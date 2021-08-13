package com.example.currencyExchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CurrencyAdapter.OnNoteListener, View.OnClickListener {

    ArrayList<ExchangeRate> currencies = new ArrayList<ExchangeRate>();
    ArrayList<ExchangeRate> myExchangeRate = new ArrayList<ExchangeRate>();

    Button date, cashless, cash, card, centralBank, payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        date = (Button)findViewById(R.id.date);
        cashless = (Button)findViewById(R.id.cashless);
        cash = (Button)findViewById(R.id.cash);
        card = (Button)findViewById(R.id.card);
        centralBank = (Button)findViewById(R.id.centralBank);
        payment = (Button)findViewById(R.id.payment);

        // устанавливаем один обработчик для всех кнопок
        date.setOnClickListener(this);
        cashless.setOnClickListener(this);
        cash.setOnClickListener(this);
        card.setOnClickListener(this);
        centralBank.setOnClickListener(this);
        payment.setOnClickListener(this);

        new AsyncRequest().execute();
    }

    // анализируем, какая кнопка была нажата
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.date:
                date.setVisibility(View.INVISIBLE);
                new AsyncRequest().execute();
                break;
            case R.id.cashless: updateRecyclerView(3,false); break;
            case R.id.cash: updateRecyclerView(4,false); break;
            case R.id.card: updateRecyclerView(6,false); break;
            case R.id.centralBank: updateRecyclerView(100,false); break;
            case R.id.payment: updateRecyclerView(300,false); break;
        }
    }
    // обновляем данные курса валют
    void updateRecyclerView (int exchangeRateType, boolean allElements) {
        myExchangeRate = new ArrayList<ExchangeRate>();
        for (ExchangeRate exchangeRate : currencies) {
            if (exchangeRate.tp == exchangeRateType || allElements) {
                myExchangeRate.add(exchangeRate);
            }
        }

        if (myExchangeRate.size() == 0) {
            Toast.makeText(this,  "Нет данных", Toast.LENGTH_SHORT).show();
            return;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        // создаем адаптер
        CurrencyAdapter adapter = new CurrencyAdapter(this, myExchangeRate, this);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
    }

    void processValue(String str) {
        Log.d("!!!!", str);

        Gson g = new Gson();
        ExchangeRatesModel exchangeRatesModel = g.fromJson(str, ExchangeRatesModel.class);

        // начальная инициализация списка
        currencies = exchangeRatesModel.rates;

        updateRecyclerView(0, true);

        Log.d("!!!!", exchangeRatesModel.downloadDate);
        date.setText(
                exchangeRatesModel.downloadDate.substring(0, 6)
                        + exchangeRatesModel.downloadDate.substring(8, 10)
                        + "\n" + exchangeRatesModel.downloadDate.substring(11));
        date.setVisibility(View.VISIBLE);
    }

    public class AsyncRequest extends AsyncTask<String, Integer, String> {
        String result = null;

        @Override
        protected String doInBackground(String... arg) {
            try {
                URL url = new URL ("https://alpha.as50464.net:29870/moby-pre-44/core?r=BEYkZbmV&d=563B4852-6D4B-49D6-A86E-B273DD520FD2&t=ExchangeRates&v=44");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Test GeekBrains iOS 3.0.0.182 (iPhone 11; iOS 14.4.1; Scale/2.00; Private)");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String jsonInputString = new JSONObject()
                        .put("uid", "563B4852-6D4B-49D6-A86E-B273DD520FD2")
                        .put("type", "ExchangeRates")
                        .put("rid", "BEYkZbmV")
                        .toString();
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    result = response.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            processValue(this.result);
        }
    }

    @Override
    public void onNoteClick(int position) {
        if (myExchangeRate.get(position).name.indexOf("/") != -1) {
            Toast.makeText(this, myExchangeRate.get(position).name, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,  "Рубль / " + myExchangeRate.get(position).name, Toast.LENGTH_SHORT).show();
        }
    }
}