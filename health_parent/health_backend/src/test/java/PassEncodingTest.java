import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jml
 * @email ziguiyue@163.com
 * @date 2022/1/5 20:06
 */
public class PassEncodingTest {
    private static BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
    public static String encoderPassword(String password){
        return  bCryptPasswordEncoder.encode(password);
    }
    public  static  void  main(String[] args){
        String password="admin";
        String pwd=encoderPassword(password);
        System.out.println(pwd);
    }
}
