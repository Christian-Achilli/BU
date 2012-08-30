package com.kp.malice.client.ui.widget;

import java.util.Date;

public interface ICalendario {
    interface Listener{
        void setDateRange(String attribute, Date start, Date finish);
    }
}