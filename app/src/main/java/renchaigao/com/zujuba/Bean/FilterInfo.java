package renchaigao.com.zujuba.Bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilterInfo {
    private String id;

    private Integer teamId;

    private String integrityScore;

    private String sexRatio;

    private String careerScreening;

    private String ageScreening;

    private String evaluationScreening;

    private String marriage;

    public void setIntegrityScore(String integrityScore) {
        this.integrityScore = integrityScore;
    }
}