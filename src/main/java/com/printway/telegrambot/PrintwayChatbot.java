package com.printway.telegrambot;

import com.printway.service.NewOrderService;
import com.printway.service.NewTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class PrintwayChatbot extends TelegramLongPollingBot {
    @Scheduled(cron = "0 30 17 ? * *")
    public void run() throws ParseException {
        SendAuto();
    }

    private String token = "token-here";
    private String username = "printwayioBot";
    private final String PRODUCT_GROUP_CHAT_ID = "-1001391001050";
    private final String TECHROOM_GROUP_CHAT_ID = "-1001236402745";
    private final String TEST_GROUP_CHAT_ID = "-1001300395182";
    private String CHAT_ID = TEST_GROUP_CHAT_ID;
//    private String CHAT_ID = TECHROOM_GROUP_CHAT_ID;

    @PostConstruct
    public void registerBot() throws TelegramApiRequestException {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            token = "token-here";
            username = "printwayadminbot";
            botsApi.registerBot(this);
            e.printStackTrace();
        }
    }

    @Autowired
    private NewOrderService newOrderService;

    @Autowired
    private NewTrackingService newTrackingService;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                String msg = message.getText().toLowerCase();
                if (msg.equals("hello")) {
                    SendMessage(message,
                            "Hello!"
                                    + "\n- Chat ID: " + message.getChatId().toString()
                                    + "\n- Name: @" + message.getFrom().getUserName());
                }
                if (msg.equals("about")) {
                    SendMessage(message,
                            "CÔNG TY TNHH PRINTWAY (PRINTWAY FACTORY)"
                                    + "\n- About Us: https://printway.io");
                }
                if (msg.equals("test") && message.getFrom().getId().equals(1631693223)) {
                    SendAuto();
                }
//                if (message.hasText()) {
//                    try {
//                        String dateEnter = message.getText();
//                        SimpleDateFormat dateEnteredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        SimpleDateFormat dateReceivedFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        Date dateEntered = dateEnteredFormat.parse(dateEnter);
//                        Date dateReceived = dateReceivedFormat.parse(dateEnter);
//                        try {
//                            SendAutoTest(dateEntered, dateReceived);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    }

    public void SendMessage(Message message, String text) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(text);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void SendMessageGroup(String chatId, String text) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            sendMessage.setParseMode("HTML");
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//    public void SendAutoTest(Date dateEntered, Date dateReceived) throws Exception {
//        try {
//            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
//            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss, E dd/MM/yyyy");
//            SimpleDateFormat dateEnteredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat dateReceivedFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String test1 = dateEnteredFormat.format(dateEntered);
//            String test2 = dateReceivedFormat.format(dateReceived);
//            Date dateEnteredTest = dateEnteredFormat.parse(test1);
//            Date dateReceivedTest = dateReceivedFormat.parse(test2);
//            String dateDisplay = formatter1.format(dateEntered);
//            String currentDateDisplay = formatter2.format(dateEntered);
//            List<Integer> printQuantity = newOrderService.countPrintQuantity(dateReceivedTest);
//            List<Integer> printedQuantity = newOrderService.countPrintedQuantity(dateReceivedTest);
//            List<Integer> packagedQuantity = newOrderService.countPackagedQuantity(dateReceivedTest);
//            List<Integer> errorQuantityIn = newOrderService.countErrorQuantityIn(dateReceivedTest);
//            List<Integer> totalOrder = newTrackingService.countOrder(dateEnteredTest);
//            List<Integer> totalLabeled = newTrackingService.countLabeled(dateEnteredTest);
//            List<Integer> totalUnlabeled = newTrackingService.countUnlabeled(dateEnteredTest);
//            List<Integer> totalShipped = newTrackingService.countShipped(dateEnteredTest);
//            List<Integer> totalUnshipped = newTrackingService.countUnshipped(dateEnteredTest);
//
//            BigDecimal print;
//            BigDecimal printed;
//            BigDecimal packaged;
//            BigDecimal error;
//            BigDecimal order;
//            BigDecimal labeled;
//            BigDecimal unlabeled;
//            BigDecimal shipped;
//            BigDecimal unshipped;
//
//            if (printQuantity.size() <= 0
//                    || printedQuantity.size() <= 0
//                    || packagedQuantity.size() <= 0
//                    || errorQuantityIn.size() <= 0
//                    || totalOrder.size() <= 0
//                    || totalLabeled.size() <= 0
//                    || totalUnlabeled.size() <= 0
//                    || totalShipped.size() <= 0
//                    || totalUnshipped.size() <= 0) {
//                print = new BigDecimal(0);
//                printed = new BigDecimal(0);
//                packaged = new BigDecimal(0);
//                error = new BigDecimal(0);
//                order = new BigDecimal(0);
//                labeled = new BigDecimal(0);
//                unlabeled = new BigDecimal(0);
//                shipped = new BigDecimal(0);
//                unshipped = new BigDecimal(0);
//            } else {
//                print = new BigDecimal(String.valueOf(printQuantity.get(0)));
//                printed = new BigDecimal(String.valueOf(printedQuantity.get(0)));
//                packaged = new BigDecimal(String.valueOf(packagedQuantity.get(0)));
//                error = new BigDecimal(String.valueOf(errorQuantityIn.get(0)));
//                order = new BigDecimal(String.valueOf(totalOrder.get(0)));
//                labeled = new BigDecimal(String.valueOf(totalLabeled.get(0)));
//                unlabeled = new BigDecimal(String.valueOf(totalUnlabeled.get(0)));
//                shipped = new BigDecimal(String.valueOf(totalShipped.get(0)));
//                unshipped = new BigDecimal(String.valueOf(totalUnshipped.get(0)));
//            }
//
//            SendMessageGroup(CHAT_ID,
//                    "*** <u><b>THỐNG KÊ "
//                            + dateDisplay
//                            + "</b></u> ***"
//                            + "\n("
//                            + "<code>"
//                            + currentDateDisplay
//                            + "</code>"
//                            + ")"
//                            + "\n-------------------------"
//                            + "\nTotal Print: "
//                            + "<b>"
//                            + print
//                            + "</b>"
//                            + "\nPrinted: "
//                            + "<b>"
//                            + printed
//                            + "</b>"
//                            + "\nPackaged: "
//                            + "<b>"
//                            + packaged
//                            + "</b>"
//                            + "\nError: "
//                            + "<b>"
//                            + error
//                            + "</b>"
//                            + "\n-------------------------"
//                            + "\nTotal Order: "
//                            + "<b>"
//                            + order
//                            + "</b>"
//                            + "\nLabeled: "
//                            + "<b>"
//                            + labeled
//                            + "</b>"
//                            + "\nUnlabeled: "
//                            + "<b>"
//                            + unlabeled
//                            + "</b>"
//                            + "\nShipped: "
//                            + "<b>"
//                            + shipped
//                            + "</b>"
//                            + "\nUnshipped: "
//                            + "<b>"
//                            + unshipped
//                            + "</b>"
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void SendAuto() {
        try {
            Date currentDate = new Date(System.currentTimeMillis());

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss, E dd/MM/yyyy");

            SimpleDateFormat dateEnteredFormat = new SimpleDateFormat("yyyy-MM-dd");

            String dateEnteredForm = dateEnteredFormat.format(new Date(System.currentTimeMillis()));
            System.out.println("dateEnteredForm: " + dateEnteredForm);

            String dateDisplay = formatter1.format(currentDate);
            String currentDateDisplay = formatter2.format(currentDate);

            List<Integer> printQuantity = newOrderService.countPrintQuantity(dateEnteredForm);
            List<Integer> printedQuantity = newOrderService.countPrintedQuantity(dateEnteredForm);
            List<Integer> packagedQuantity = newOrderService.countPackagedQuantity(dateEnteredForm);
            List<Integer> errorQuantityIn = newOrderService.countErrorQuantityIn(dateEnteredForm);
            List<Integer> totalOrder = newTrackingService.countOrder(dateEnteredForm);

            List<Integer> totalLabeled = newTrackingService.countLabeled(dateEnteredForm);
            List<Integer> totalUnlabeled = newTrackingService.countUnlabeled(dateEnteredForm);
            List<Integer> totalShipped = newTrackingService.countShipped(dateEnteredForm);
            List<Integer> totalUnshipped = newTrackingService.countUnshipped(dateEnteredForm);

            BigDecimal print;
            BigDecimal printed;
            BigDecimal packaged;
            BigDecimal error;
            BigDecimal order;
            BigDecimal labeled;
            BigDecimal unlabeled;
            BigDecimal shipped;
            BigDecimal unshipped;

            if (printQuantity.size() == 0 || printQuantity.get(0) == null) {
                print = new BigDecimal(0);
            } else {
                print = new BigDecimal(String.valueOf(printQuantity.get(0)));
            }

            if (printedQuantity.size() == 0 || printedQuantity.get(0) == null) {
                printed = new BigDecimal(0);
            } else {
                printed = new BigDecimal(String.valueOf(printedQuantity.get(0)));
            }

            if (packagedQuantity.size() == 0 || packagedQuantity.get(0) == null) {
                packaged = new BigDecimal(0);
            } else {
                packaged = new BigDecimal(String.valueOf(packagedQuantity.get(0)));
            }

            if (errorQuantityIn.size() == 0 || errorQuantityIn.get(0) == null) {
                error = new BigDecimal(0);
            } else {
                error = new BigDecimal(String.valueOf(errorQuantityIn.get(0)));
            }

            if (totalOrder.size() == 0 || totalOrder.get(0) == null) {
                order = new BigDecimal(0);
            } else {
                order = new BigDecimal(String.valueOf(totalOrder.get(0)));
            }

            if (totalLabeled.size() == 0 || totalLabeled.get(0) == null) {
                labeled = new BigDecimal(0);
            } else {
                labeled = new BigDecimal(String.valueOf(totalLabeled.get(0)));
            }

            if (totalUnlabeled.size() == 0 || totalUnlabeled.get(0) == null) {
                unlabeled = new BigDecimal(0);
            } else {
                unlabeled = new BigDecimal(String.valueOf(totalUnlabeled.get(0)));
            }

            if (totalShipped.size() == 0 || totalShipped.get(0) == null) {
                shipped = new BigDecimal(0);
            } else {
                shipped = new BigDecimal(String.valueOf(totalShipped.get(0)));
            }

            if (totalUnshipped.size() == 0 || totalUnshipped.get(0) == null) {
                unshipped = new BigDecimal(0);
            } else {
                unshipped = new BigDecimal(String.valueOf(totalUnshipped.get(0)));
            }

            SendMessageGroup(CHAT_ID,
                    "*** <u><b>THỐNG KÊ "
                            + dateDisplay
                            + "</b></u> ***"
                            + "\n("
                            + "<code>"
                            + currentDateDisplay
                            + "</code>"
                            + ")"
                            + "\n-------------------------"
                            + "\nTotal Print: "
                            + "<b>"
                            + print
                            + "</b>"
                            + "\nPrinted: "
                            + "<b>"
                            + printed
                            + "</b>"
                            + "\nPackaged: "
                            + "<b>"
                            + packaged
                            + "</b>"
                            + "\nError: "
                            + "<b>"
                            + error
                            + "</b>"
                            + "\n-------------------------"
                            + "\nTotal Order: "
                            + "<b>"
                            + order
                            + "</b>"
                            + "\nLabeled: "
                            + "<b>"
                            + labeled
                            + "</b>"
                            + "\nUnlabeled: "
                            + "<b>"
                            + unlabeled
                            + "</b>"
                            + "\nShipped: "
                            + "<b>"
                            + shipped
                            + "</b>"
                            + "\nUnshipped: "
                            + "<b>"
                            + unshipped
                            + "</b>"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
