package com.example.currencyExchange;

import java.util.ArrayList;

public class ExchangeRatesModel {
    int code;
    String messageTitle;
    String rid;
    /// Описание отказа
    //public String rejectMessage;
    /// Состояние продуктов для обмена
    //public ProductExchangeState productState;
    /// Дата загрузки
    String downloadDate;
    /// Список курсов валют
    ArrayList<ExchangeRate> rates;
}

class ProductExchangeState {
    /// 0 - нет инф-и о продуктах (не отображать кнопку)
    int none = 0;
    /// 1 - есть валютные счета (возможен перевод)
    int transfer = 1;
    /// 2 - нет валютных счетов (предлагаем открыть счет)
    int openAccount = 2;
}

class ExchangeRate {
    /// Код типа курса
    //public ExchangeRateType tp;
    int tp;
    /// Название от АБС
    String name;
    /// Код валюты базы
    int from;
    /// Валюта базы
    //public CurrencyMnemType currMnemFrom;
    String currMnemFrom;
    /// Код валюты курса
    int to;
    /// Валюта курса
    //public CurrencyMnemType currMnemTo;
    String currMnemTo;
    /// Количество котируемой валюты
    String basic;
    /// Покупка
    String buy;
    /// Продажа
    String sale;
    /// Динамика покупки
    String deltaBuy;
    /// Динамика продажи
    String deltaSale;

    public String getExchangeRateType(){
        switch (tp){
            case 3:
                return "cashless";
            case 4:
                return "cash";
            case 6:
                return "card";
            case 100:
                return "centralBank";
            case 300:
                return "payment";
            default:
                return "nothing";
        }

    }
}

class ExchangeRateType {
    /// Безналичные(3)
    //cashless (3),
    /// Наличный(4)
    //cash (4),
    /// По картам Банка(6)
    //card (6),
    /// Курсы ЦБ РФ(100)
    //centralBank (100),
    /// Для расчета при переводах
    //payment (300);
}
