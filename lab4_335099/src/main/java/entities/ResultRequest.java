package entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultRequest {
    public String x;
    public String y;
    public String r;
    public Boolean fromCanvas;
}
