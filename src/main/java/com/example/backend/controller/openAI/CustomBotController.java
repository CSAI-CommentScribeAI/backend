package com.example.backend.controller.openAI;

import com.example.backend.dto.openAI.ChatGPTRequestDTO;
import com.example.backend.dto.openAI.ChatGPTResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bot")
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.apiurl}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt")String prompt){
        prompt ="고객에게 배달 서비스로 사용할 짧은 편지를 작성하는데 도움이 필요합니다. 아래 정보를 바탕으로 개인화된 편지를 작성해 이 편지는 배달이 갈 때 같이가는 내용이니 음식을 먹기전에 보는 내용이야\n\n"+
                "식당 정보:\n" +
                "- 식당 이름: 피자헛\n" +
                "\n" +
                "회원 정보:\n" +
                "- 회원 이름: 피자이스\n" +
                "- 주문 횟수: 4번\n" +
                "- 최근 리뷰(최대 10개):\n" +
                "1. 오늘 따라 치즈양이 적었고 피클도 안왔어요\n" +
                "2. 항상 맛있게 먹고 있어요\n" +
                "3. 피자는 여기서만 시켜먹어야 겠어요\n" +
                "4.다른 지점과 달리 치즈와 토핑양이 많아요\n" +
                "5.\n" +
                "6.\n" +
                "7.\n" +
                "8.\n" +
                "9.\n" +
                "10.\n" +
                "\n" +
                "주문 내역:\n" +
                "- 포테이토 피자\n" +
                "- 콜라 1L\n" +
                "\n" +
                "편지는 가벼운 느낌으로 간단하게 작서하고 최대한 친근하게 작성해 그리고 리뷰내용을 중점으로 작성해 최대한 1번내용에 집중해서 작성해 1번 내용은 오늘 주문 내용에 대한 리뷰가 아니라 바로 전에 주문 했을 때 의 리뷰야 그리고 친근감 있게 이모티콘도 사용해 마지막에는 식당이름 드림으로 끝내 그리고 주문내역은 이번에 주문한 내역이고 리뷰는 전에 배달했던 것에 대한 내용이야 전혀 상관없어 ";

        ChatGPTRequestDTO request = new ChatGPTRequestDTO(model, prompt);
        System.out.println(request);
        ChatGPTResponseDTO chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponseDTO.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
