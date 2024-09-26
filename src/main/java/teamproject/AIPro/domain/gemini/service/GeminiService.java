package teamproject.AIPro.domain.gemini.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import teamproject.AIPro.domain.gemini.dto.request.ChatRequest;
import teamproject.AIPro.domain.gemini.dto.response.ChatResponse;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getContents(String instruction, String prompt) {

        // Gemini에 요청 전송
        String requestUrl = apiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest("너는 911 상담원이야 신고자가 직접적으로 도움을 요청 할 수 없는 상황이야 예를 들어 음식 주문으로 연기를 한다던가 말을 안하고 톡톡 소리만 낼 수 있어", prompt);
        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);
        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();
        return message;
    }
}