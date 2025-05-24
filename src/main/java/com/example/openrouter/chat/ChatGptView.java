package com.example.openrouter.chat;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;

import com.vaadin.flow.component.html.Div;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.ollama.OllamaChatModel;

@Route("/chat-gpt")
@CssImport("./styles/styles.css")

public class ChatGptView extends VerticalLayout {
    private VerticalLayout messagesContainer;
    private TextField messageInput;
   // private final OllamaChatModel chatModel;
private ChatClient chatModel;
   // public ChatGptView(OllamaChatModel chatModel) {
    public ChatGptView(ChatClient chatModel) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Messages container
        messagesContainer = new VerticalLayout();
        messagesContainer.addClassName("messages-container");
        messagesContainer.setPadding(false);
        messagesContainer.setSpacing(true);
        messagesContainer.setHeightFull();
        messagesContainer.getStyle().set("overflow-y", "auto");

        // Input area
        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.setWidthFull();
        inputLayout.setPadding(true);
        inputLayout.addClassName("input-area");
this.chatModel=chatModel;
        messageInput = new TextField();
        messageInput.setPlaceholder("Type your message...");
        messageInput.setWidthFull();

        Button sendButton = new Button("Send", VaadinIcon.ARROW_UP.create());
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        inputLayout.add(messageInput, sendButton);
        inputLayout.setFlexGrow(1, messageInput);

        // Add components
        add(messagesContainer, inputLayout);
        setFlexGrow(1, messagesContainer);

        // Send action
        sendButton.addClickListener(e -> sendMessage());
       // messageInput.addSubmitListener(e -> sendMessage());
    }

    private void sendMessage() {
        String text = messageInput.getValue().trim();
        if (!text.isEmpty()) {
            addMessage(text, true);  // User message
            messageInput.clear();

            // Simulate response (replace with actual API call)
            UI ui = UI.getCurrent();
            ui.access(() -> {
             //   addMessage("Simulated response", false);
                addMessage(chatModel.prompt(text).call().content(), false);
            });
      /*      new Thread(() -> {
            //    try {
                //    Thread.sleep(1000);

                    // Use the captured UI reference
                    ui.access(() -> {
                        addMessage("Simulated response", false);
                    });
               *//* }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }*//*
            }).start();*/
         /*   new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    UI.getCurrent().access(() -> {
                        addMessage("This is a simulated response", false);  // Bot message
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();*/
        }
    }

    private void addMessage(String text, boolean isUser) {
        Div messageDiv = new Div();
        messageDiv.addClassName(isUser ? "user-message" : "assistant-message");
        messageDiv.setText(text);
        messagesContainer.add(messageDiv);

        // Scroll to bottom
        UI.getCurrent().getPage().executeJs(
                "setTimeout(function() {" +
                        "   const container = $0;" +
                        "   container.scrollTop = container.scrollHeight;" +
                        "}, 100);",
                messagesContainer.getElement()
        );
    }
}

