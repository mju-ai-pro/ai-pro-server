package teamproject.AIPro.domain.gemini.dto.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequest {
    private SystemInstruction system_instruction;  // system_instruction 필드 추가
    private List<Content> contents;
    private GenerationConfig generationConfig;

    // SystemInstruction 클래스 추가
    @Getter
    @Setter
    public static class SystemInstruction {
        private Parts parts;
    }

    @Getter
    @Setter
    public static class Content {
        private Parts parts;
    }

    @Getter
    @Setter
    public static class Parts {
        private String text;
    }

    @Getter
    @Setter
    public static class GenerationConfig {
        private int candidate_count;
        private int max_output_tokens;
        private double temperature;
    }

    // 기존 생성자에서 system_instruction도 추가
    public ChatRequest(String systemText, String prompt) {
        // system_instruction 설정
        this.system_instruction = new SystemInstruction();
        Parts systemParts = new Parts();
        systemParts.setText(systemText);
        this.system_instruction.setParts(systemParts);

        // contents 설정
        this.contents = new ArrayList<>();
        Content content = new Content();
        Parts parts = new Parts();
        parts.setText(prompt);
        content.setParts(parts);
        this.contents.add(content);

        // generationConfig 설정
        this.generationConfig = new GenerationConfig();
        this.generationConfig.setCandidate_count(1);
        this.generationConfig.setMax_output_tokens(1000);
        this.generationConfig.setTemperature(0.7);
    }
}