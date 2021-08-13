package com.example.currencyExchange;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CurrencyAdapter  extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final ArrayList<ExchangeRate> currencies;
    private OnNoteListener mOnNoteListener;

    CurrencyAdapter(Context context, ArrayList<ExchangeRate> currencies, OnNoteListener onNoteListener) {
        this.currencies = currencies;
        this.inflater = LayoutInflater.from(context);
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(CurrencyAdapter.ViewHolder holder, int position) {
        ExchangeRate currency = currencies.get(position);
        holder.nameView.setText(currency.currMnemFrom + "/" + currency.currMnemTo);
        holder.buyView.setText(currency.buy);
        holder.saleView.setText(currency.sale);
        holder.deltaBuyView.setText(currency.deltaBuy);
        holder.deltaSaleView.setText(currency.deltaSale);
        // изменение цвета динамики покупки
        if (Double.parseDouble(currency.deltaBuy) < 0) {
            holder.deltaBuyView.setTextColor(Color.RED);
        }
        else if (Double.parseDouble(currency.deltaBuy) > 0) {
            holder.deltaBuyView.setTextColor(Color.GREEN);
        }
        // изменение цвета динамики продажи
        if (Double.parseDouble(currency.deltaSale) < 0) {
            holder.deltaSaleView.setTextColor(Color.RED);
        }
        else if (Double.parseDouble(currency.deltaSale) > 0)
        {
            holder.deltaSaleView.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nameView, buyView, saleView, deltaBuyView, deltaSaleView;
        OnNoteListener onNoteListener;

        ViewHolder(View view, OnNoteListener onNoteListener){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            buyView = (TextView) view.findViewById(R.id.buy);
            saleView = (TextView) view.findViewById(R.id.sale);
            deltaBuyView  = (TextView) view.findViewById(R.id.deltaBuy);
            deltaSaleView = (TextView) view.findViewById(R.id.deltaSale);
            this.onNoteListener = onNoteListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAbsoluteAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}