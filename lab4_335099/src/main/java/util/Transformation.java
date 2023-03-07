package util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@UtilityClass
public class Transformation {
    private final int OKRUGLENIE = 2;
    private final int RANDOM = 10;

    public String stringRandom(int count){
        if (count <= 0){
            count = RANDOM;
        }
        return new SecureRandom().ints('0', 'z').
                filter(sym ->  (sym >= 'A' && sym <= 'Z') || (sym >= 'a' && sym <= 'z') || (sym >= '0' && sym <= '9')).limit(count).
                collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    public double roundDouble(String number){
        double value = Double.parseDouble(number);
        double answer = new BigDecimal(value).setScale(OKRUGLENIE, RoundingMode.HALF_UP).doubleValue();
        return answer;
    }

    public double roundDouble(double number){
        double answer = new BigDecimal(number).setScale(OKRUGLENIE, RoundingMode.HALF_UP).doubleValue();
        return answer;
    }

    public boolean validate(double x, double y, double r, boolean fromCanvas){
        return (((x >= -4 && x <= 4) && (y >= -5 && y <= 5)) || fromCanvas) && (r >= -4 && r <= 4);
    }

    public boolean checkInArea(double x, double y, double R){
        return (inSquare(x, y, R) || inTriangle(x, y, R) || inCircle(x, y, R));
    }

    private boolean inSquare(double x, double y, double R){
        if (R >= 0) {
            return (x >= 0 && y <= 0 && x <= R/2 && y >= -R);
        }
        else{
            return (x <= 0 && y >= 0 && x >= R/2 && y <= -R);
        }
    }

    private boolean inTriangle(double x, double y, double R){
        if (R >= 0) {
            return (x <= 0 && y >= 0 && y <= x + R / 2);
        }
        else{
            return (x >= 0 && y <= 0 && y >= x + R / 2);
        }
    }

    private boolean inCircle(double x, double y, double R){
        if (R >= 0) {
            return (x <= 0 && y <= 0 && Math.sqrt(x * x + y * y) <= R / 2);
        }
        else{
            return (x >= 0 && y >= 0 && Math.sqrt(y * y + x * x) <= (-R / 2));
        }
    }

}
