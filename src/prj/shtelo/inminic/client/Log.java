package prj.shtelo.inminic.client;

public class Log {
    private static boolean SHOW_LOG = true;
    Log(){

    }
    public static void v(String tag, String log){
        System.out.println(tag+"_verbose : "+log);
    }
    public static void d(String tag, String log){
        System.out.println(tag+"_debug : "+log);
    }
    public static void i(String tag, String log){
        System.out.println(tag+"_info : "+log);
    }
    public static void w(String tag, String log){
        System.out.println(tag+"_warn : "+log);
    }
    public static void e(String tag, String log){
        System.out.println(tag+"_error : "+log);
    }
    public static void a(String tag, String log){
        System.out.println(tag+"_assert : "+log);
    }
    public void showLog(boolean showLog){
        this.SHOW_LOG = showLog;
    }
}
