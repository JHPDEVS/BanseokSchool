package com.jhp.banseok.bap.spreadsheets;

import android.os.AsyncTask;
import android.util.Log;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.net.URL;
import java.util.List;

/**
 * Created by whdghks913 on 2015-10-02.
 */
public abstract class GoogleSheetTask extends AsyncTask<String, Integer, Long> {
    /**
     * --------- Row
     * --------- Row
     * | | | | | Column
     * | | | | | Column
     */
    public int startRowNumber = 0;
    public int startColumnNumber = 0;

    public abstract void onPreDownload();

//    public abstract void onUpdate(int params);

    public abstract void onFinish(long result);

    public abstract void onRow(int startRowNumber, int position, String[] row);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onPreDownload();
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            Source mSource = null;

            mSource = new Source(new URL(params[0]));
            mSource.fullSequentialParse();

            List<Element> tables = mSource.getAllElements(HTMLElementName.TABLE);
            Element table = tables.get(0);

            if (table.getAttributeValue("class").equals("waffle")) {
                Element tbody = table.getAllElements(HTMLElementName.TBODY).get(0);

                List<Element> tr = tbody.getAllElements(HTMLElementName.TR);

                int tr_size = tr.size();
                for (int i = startRowNumber; i < tr_size; i++) {
                    List<Element> td = tr.get(i).getAllElements(HTMLElementName.TD);
                    String[] sheetData = new String[td.size()];

                    int td_size = td.size();
                    for (int j = startColumnNumber; j < td_size; j++) {
                        Element data = td.get(j);
                        String mRow = data.getContent().toString().trim();
                        mRow = mRow.replace("<br>", "\n");
                        sheetData[j] = RemoveHTMLTag(mRow);
                    }

                    onRow(startRowNumber, i, sheetData);
                }
            }

        } catch (Exception e) {
            Log.e("GoogleSheetTask Error", "Message : " + e.getMessage());
            Log.e("GoogleSheetTask Error", "LocalizedMessage : " + e.getLocalizedMessage());

            e.printStackTrace();
            return -1l;
        }
        return 0l;
    }

    @Override
    protected void onProgressUpdate(Integer... params) {
//        onUpdate(params[0]);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        onFinish(result);
    }

    public String RemoveHTMLTag(String changeStr) {
        if (changeStr != null && !changeStr.equals("")) {
            changeStr = changeStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        } else {
            changeStr = "";
        }
        return changeStr;
    }
}
