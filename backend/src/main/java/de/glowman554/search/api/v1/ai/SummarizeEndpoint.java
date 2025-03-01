package de.glowman554.search.api.v1.ai;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.data.WebPage;
import de.glowman554.search.utils.openai.ChatApi;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class SummarizeEndpoint extends BaseHandler {
    private final String systemPrompt = "Summarize the text in at most one sentence. You can not use Markdown or HTML.";

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        premium(ctx);

        String link = ctx.queryParam("l");
        if (link == null || link.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing link parameter");
            return;
        }

        WebPage page = Main.getDatabase().loadWebPage(link);
        if (page == null) {
            throw new Exception("Web page not found");
        }

        ChatApi.Message[] messages = new ChatApi.Message[]{
                new ChatApi.Message("system", systemPrompt),
                new ChatApi.Message("user", page.getTitle() + "\n\n" + page.getContent())
        };

        ChatApi.Message message = Main.getChatApi().requestCompletion(messages)[0];

        json(ctx, JsonNode.object().set("summary", message.getContent()));
    }
}
