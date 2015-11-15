package com.example.cse5324.newdiary2;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by oguni on 11/11/2015.
 */
public class MyPrintDocumentAdapter extends PrintDocumentAdapter
{
    private static final int MILS_IN_INCH = 1000;
    private static final float HEADER_SIZE = 40;
    private static final float CONTENT_SIZE = 14;
    private static final float LINE_HEIGHT = 35;
    private static final int CHARS_PER_LINE = 50;
    Context mPrintContext;
    public PdfDocument myPdfDocument;
    public int totalpages;
    private MyListItem item;
    private ArrayList<MyListItem> children;
    private int mRenderPageWidth;
    private int mRenderPageHeight;
    private PrintDocumentInfo mDocumentInfo;

    public MyPrintDocumentAdapter(Context context, MyListItem item, ArrayList<MyListItem> children)
    {
        mPrintContext = context;
        this.children = children;
        this.item = item;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {

        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        totalpages = 1;
        if (children != null){
            totalpages += children.size();
        }

        boolean layoutNeeded = false;
        final int density = Math.max(newAttributes.getResolution().getHorizontalDpi(),
                newAttributes.getResolution().getVerticalDpi());
        final int marginLeft = (int) (density * (float) newAttributes.getMinMargins()
                .getLeftMils() / MILS_IN_INCH);
        final int marginRight = (int) (density * (float) newAttributes.getMinMargins()
                .getRightMils() / MILS_IN_INCH);
        final int contentWidth = (int) (density * (float) newAttributes.getMediaSize()
                .getWidthMils() / MILS_IN_INCH) - marginLeft - marginRight;
        if (mRenderPageWidth != contentWidth) {
            mRenderPageWidth = contentWidth;
            layoutNeeded = true;
        }

        final int marginTop = (int) (density * (float) newAttributes.getMinMargins()
                .getTopMils() / MILS_IN_INCH);
        final int marginBottom = (int) (density * (float) newAttributes.getMinMargins()
                .getBottomMils() / MILS_IN_INCH);
        final int contentHeight = (int) (density * (float) newAttributes.getMediaSize()
                .getHeightMils() / MILS_IN_INCH) - marginTop - marginBottom;
        if (mRenderPageHeight != contentHeight) {
            mRenderPageHeight = contentHeight;
            layoutNeeded = true;
        }

        if (mPrintContext.getResources().getConfiguration().densityDpi != density) {
            Configuration configuration = new Configuration();
            configuration.densityDpi = density;
            mPrintContext = mPrintContext.createConfigurationContext(configuration);
            mPrintContext.setTheme(android.R.style.Theme_Holo_Light);
        }

        if (!layoutNeeded) {
            callback.onLayoutFinished(mDocumentInfo, false);
            return;
        }

        int charsPerLine = 30;
        int currentPage = 0;
        int pageContentHeight = 0;
        if (item.getPicPath().equals("")){
            pageContentHeight += mRenderPageHeight/2;
        }
        pageContentHeight += HEADER_SIZE;
        String text = item.getFormatted();
        String[] textArray = text.split("\n");
        int currentWidth = 0;

        PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

        PrintDocumentInfo info = builder.build();
        callback.onLayoutFinished(info, true);
    }


    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {

        // If we are already cancelled, don't do any work.
        if (cancellationSignal.isCanceled()) {
            callback.onWriteCancelled();
            return;
        }

        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pageRanges, i))
            {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(mRenderPageWidth,
                        mRenderPageHeight, i).create();
                PdfDocument.Page page = myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }
        callback.onWriteFinished(pageRanges);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        for (int i = 0; i<pageRanges.length; i++)
        {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page, int pagenumber) {
        Canvas canvas = page.getCanvas();
        pagenumber++; // Make sure page numbers start at 1
        MyListItem item;
        if (pagenumber == 1){
            item = this.item;
        } else {
            item = children.get(pagenumber - 2);
        }

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(HEADER_SIZE);
        canvas.drawText(item.getName(), leftMargin, titleBaseLine, paint);

        paint.setTextSize(CONTENT_SIZE);
        String text = item.getFormatted();
        String[] result = text.split("\n");

        for (String x : result){
            StringTokenizer tokenizer = new StringTokenizer(x);
            String token = "";
            int tokenSize = 0;
            while (tokenizer.hasMoreTokens()){
                String nextToken = tokenizer.nextToken();
                tokenSize += nextToken.length();
                if (tokenSize > CHARS_PER_LINE){
                    canvas.drawText(token, leftMargin, titleBaseLine += 35, paint);
                    token = nextToken;
                    tokenSize = nextToken.length();
                }
            }
        }

        if (pagenumber > 1){
            return;
        }
        int pageHeight = page.getInfo().getPageHeight();
        int pageWidth = page.getInfo().getPageWidth();
        Bitmap image = item.getBitmapPic();
        if (image != null){
            canvas.drawBitmap(image, new Rect(0,0,image.getWidth(),image.getHeight()),
                    new Rect(leftMargin, pageHeight/2, pageWidth, pageHeight), paint);
            image.recycle();
        }
    }



}
