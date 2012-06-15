package org.apache.hupa.client.ui;


import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.LayoutPanel;

public interface AppLayout {

  LayoutPanel getMainLayoutPanel();

  AcceptsOneWidget getWestContainer();

  AcceptsOneWidget getMainContainer();

  void setLoginLayout();

  void setDefaultLayout();

}
