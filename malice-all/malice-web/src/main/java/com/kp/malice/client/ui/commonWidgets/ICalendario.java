package com.kp.malice.client.ui.commonWidgets;

import java.util.Date;

public interface ICalendario {
    interface Listener{
        void setDateRange(String attribute, Date start, Date finish);
    }
}