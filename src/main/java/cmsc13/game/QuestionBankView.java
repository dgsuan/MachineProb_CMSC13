package cmsc13.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/** Course-style review hub and sequential lesson reader. */
final class QuestionBankView extends BorderPane {
    private final QuestionBank bank;
    private final Runnable onBack;
    private final List<Topic> topics = loadTopics();
    private final VBox page = new VBox(18);
    private String track;
    private Topic topic;
    private int index;

    QuestionBankView(QuestionBank bank, Runnable onBack) {
        this.bank=bank; this.onBack=onBack;
        setPadding(new Insets(28));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #111927, #263d4a);");
        page.setAlignment(Pos.TOP_CENTER); page.setMaxWidth(1050);
        ScrollPane scroll=new ScrollPane(page); scroll.setFitToWidth(true); scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        setCenter(scroll); showHub();
    }

    private void showHub() {
        page.getChildren().setAll(heading("REVIEW TOPICS"), text("Choose a course track to begin.",16),
            button("THEORY",()->showTracks("THEORY")),button("PROGRAMMING / PRACTICAL",()->showTracks("PROGRAMMING")),button("BACK TO MENU",onBack));
    }
    private void showTracks(String selected) {
        track=selected;
        List<Topic> list=topics.stream().filter(t->t.track.equals(track)).collect(Collectors.toList());
        VBox cards=new VBox(10); cards.setMaxWidth(820);
        for(Topic t:list) { Button b=button(String.format("%02d   %s",t.trialId,t.title),()->{topic=t;index=0;showLesson();}); b.setMaxWidth(Double.MAX_VALUE); cards.getChildren().add(b); }
        page.getChildren().setAll(heading(track.equals("THEORY")?"THEORY TRACK":"PROGRAMMING / PRACTICAL TRACK"),cards,button("BACK",this::showHub));
    }
    private void showLesson() {
        Card card=topic.cards.get(index);
        Label title=text(topic.title,25); title.setStyle(title.getStyle()+"-fx-font-weight: bold;");
        Label progress=text("Card "+(index+1)+" / "+topic.cards.size()+"     ·     "+card.title,16);
        ProgressBar bar=new ProgressBar((double)(index+1)/topic.cards.size());bar.setMaxWidth(760);
        VBox box=new VBox(16,text(card.title.toUpperCase(),21),renderBody(card.body));box.setPadding(new Insets(28));box.setMaxWidth(850);box.setStyle("-fx-background-color: rgba(245,249,247,.97); -fx-background-radius: 14;");
        Button back=button("BACK",()->{if(index>0){index--;showLesson();}else showTracks(track);});back.setDisable(index==0);
        Button next=button(index==topic.cards.size()-1?"FINISH":"NEXT",()->{if(index<topic.cards.size()-1){index++;showLesson();}else showComplete();});
        HBox nav=new HBox(18,back,next);nav.setAlignment(Pos.CENTER);
        page.getChildren().setAll(button("‹ TOPICS",()->showTracks(track)),title,progress,bar,box,nav);
    }
    private void showComplete() {
        page.getChildren().setAll(heading("TOPIC COMPLETE"),text("You completed: "+topic.title,20),
            button("REVIEW AGAIN",()->{index=0;showLesson();}),button("QUICK CHECK",this::quickCheck),button("BACK TO TOPICS",()->showTracks(track)));
    }
    private void quickCheck() {
        List<Question> qs=bank.getQuestionsForTrial(topic.trialId).stream().filter(q->q.getType()==(track.equals("THEORY")?QuestionType.THEORY:QuestionType.PROGRAMMING)).collect(Collectors.toList());
        if(qs.isEmpty()){showTracks(track);return;} Question q=qs.get((int)(Math.random()*qs.size()));
        ChoiceDialog<String> d=new ChoiceDialog<>(q.getChoices().get(0),q.getChoices());d.setTitle("Quick Check");d.setHeaderText(q.getQuestionText());d.setContentText("Choose an answer:");
        d.showAndWait().ifPresent(answer->{Alert a=new Alert(Alert.AlertType.INFORMATION);a.setTitle("Quick Check");a.setHeaderText(answer.equals(q.getCorrectChoiceText())?"Correct":"Review this concept");a.setContentText("Answer: "+q.getCorrectChoiceText()+"\n\n"+q.getExplanation());a.showAndWait();});
        showComplete();
    }
    private static VBox renderBody(String raw) {
        VBox content=new VBox(9); boolean code=false;StringBuilder block=new StringBuilder();
        for(String line:raw.split("\\R",-1)) {
            if(line.trim().startsWith("```")) { if(code){Label l=text(block.toString(),15);l.setStyle("-fx-font-family: 'Consolas'; -fx-background-color: #17252d; -fx-text-fill: #e8f4ef; -fx-padding: 14;");content.getChildren().add(l);block.setLength(0);} code=!code;continue; }
            if(code){block.append(line).append('\n');continue;}
            String s=line.replaceAll("^#{1,6}\\s*","").replaceAll("^[-*]\\s+","• ").replaceAll("\\*\\*(.*?)\\*\\*","$1").replaceAll("\\*(.*?)\\*","$1").replaceAll("^>\\s?","");
            if(!s.isEmpty()&&!s.matches("^[| :\\-]+$")) {Label l=text(s,16);content.getChildren().add(l);}
        }
        return content;
    }
    private static Label heading(String s){Label l=text(s,28);l.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #e8f4ef;");return l;}
    private static Label text(String s,int size){Label l=new Label(s);l.setWrapText(true);l.setMaxWidth(Double.MAX_VALUE);l.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: "+size+"; -fx-text-fill: #20313a;");return l;}
    private static Button button(String s,Runnable r){Button b=new Button(s);b.setMinHeight(42);b.setOnAction(e->r.run());return b;}
    private static List<Topic> loadTopics(){
        List<Topic> result=new ArrayList<>();
        try(BufferedReader in=new BufferedReader(new InputStreamReader(QuestionBankView.class.getResourceAsStream("/Paradigm_Trials_Review_Course.md"),StandardCharsets.UTF_8))){
            String line;Topic topic=null;Card card=null;StringBuilder body=new StringBuilder();
            while((line=in.readLine())!=null){
                if(line.matches("# (TOPIC|PRACTICAL TOPIC) [0-9]+ .*")){if(card!=null){card.body=body.toString().trim();topic.cards.add(card);}String[] bits=line.replaceFirst("^# ","").split(" — ",2);int n=Integer.parseInt(bits[0].replaceAll("\\D+",""));topic=new Topic(bits[1],bits[0].startsWith("PRACTICAL")?"PROGRAMMING":"THEORY",n);result.add(topic);card=null;body.setLength(0);continue;}
                if(topic==null)continue;
                if(line.startsWith("## Card ")){if(card!=null){card.body=body.toString().trim();topic.cards.add(card);}String[] bits=line.split(" — ",2);card=new Card(bits.length>1?bits[1]:line);body.setLength(0);continue;}
                if(card!=null)body.append(line).append('\n');
            }
            if(card!=null){card.body=body.toString().trim();topic.cards.add(card);}
        }catch(Exception e){throw new IllegalStateException("Could not load review course",e);}return result;
    }
    private static final class Topic{final String title,track;final int trialId;final List<Card> cards=new ArrayList<>();Topic(String t,String tr,int id){title=t;track=tr;trialId=id;}}
    private static final class Card{final String title;String body="";Card(String t){title=t;}}
}
