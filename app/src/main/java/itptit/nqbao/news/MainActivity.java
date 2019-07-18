package itptit.nqbao.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> arrayListTitle;
    private ListView lvTitle;
    private ArrayAdapter adapter;
    private ArrayList<String> arrayListLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maps();
        new ReadRSS().execute("https://vnexpress.net/rss/the-thao.rss");
    }

    private void maps() {
        lvTitle = findViewById(R.id.lv_title);
        arrayListTitle = new ArrayList<>();
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayListTitle);
        lvTitle.setAdapter(adapter);
        arrayListLink = new ArrayList<>();

        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra("link", arrayListLink.get(i));
                startActivity(intent);
            }
        });
    }

    private class ReadRSS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();

            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");

            String title = "";
            String link = "";
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element, "title");
                arrayListTitle.add(title);
                link = parser.getValue(element, "link");
                arrayListLink.add(link);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
