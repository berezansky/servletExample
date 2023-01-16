package com.berezanskiy.XO;

import com.berezanskiy.XO.servlets.HelloServlet;
import com.berezanskiy.XO.servlets.UserServlet;
import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;

import java.io.File;

public class Main {
    private static void addServlet(Context ctx, String pattern, String servletName, HttpServlet servlet) {
        Tomcat.addServlet(ctx, servletName, servlet);
        ctx.addServletMappingDecoded(pattern, servletName);
    }

    private static Context getContext(Tomcat app) {
        File base = new File("/Users/i.berezansky/Projects/Home/Java/servlet/src/main/webapp");

        Context ctx = app.addWebapp("", base.getAbsolutePath());

        ctx.getJarScanner().setJarScanFilter(new JarScanFilter() {
            @Override
            public boolean check(JarScanType jarScanType, String jarName) {
                return false;
            }

            @Override
            public boolean isSkipAll() {
                return true;
            }
        });

        return ctx;
    }

    private static Tomcat getApp(int port) {
        Tomcat app = new Tomcat();

        app.getConnector().setPort(port);
        app.setBaseDir(System.getProperty("java.io.tmpdir"));

        Context ctx = getContext(app);

        addServlet(ctx, "", HelloServlet.class.getSimpleName(), new HelloServlet());
        addServlet(ctx, "/users/*", UserServlet.class.getSimpleName(), new UserServlet());

        return app;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat app = getApp(8080);

        app.start();
        app.getServer().await();
    }
}
