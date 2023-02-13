


import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar {
    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date date;
    public static String dayandtime;
    //public static TextView TitleTimeText;
    //使用前请绑定控件
    //TitleTimeText = findViewById(R.id.title_2_time);
    public Calendar() {
        new Thread(() -> {
            while (true) {
                Calendar.date=new Date(System.currentTimeMillis());
                dayandtime=simpleDateFormat.format(date);
                //TitleTimeText.setText(CalendarTime.dayandtime);
                //Message ms=new Message();
                //ms.arg1=0x1000;
                //ms.obj=dayandtime;
            /*    if(FiveNoseController_Main.myHandler!=null){
                    FiveNoseController_Main.myHandler.sendMessage(ms);
                }*/
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
