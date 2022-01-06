import org.junit.Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/6 9:56
 */
public class DateTest {
    @Test
    public void getFirstAndLastDayOfMonth() throws ParseException {
        String date = "2020-02-01";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String firstDayOfMonth = sdf.format(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        String lastDayOfMonth = sdf.format(calendar.getTime());
        System.out.println(firstDayOfMonth);
        System.out.println(lastDayOfMonth);
    }
}
