package de.glowman554.search.api.v1.ai;

import de.glowman554.search.Main;
import de.glowman554.search.api.BaseHandler;
import de.glowman554.search.utils.openai.ChatApi;
import io.javalin.http.Context;
import net.shadew.json.JsonNode;
import org.jetbrains.annotations.NotNull;

public class AnswerEndpoint extends BaseHandler {
    private final String systemPrompt = "Answer the user prompt to the best of your ability. The user can not answer back. You can not use Markdown or HTML.";

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        premium(ctx);

        String query = ctx.queryParam("q");
        if (query == null || query.isEmpty()) {
            ctx.status(400);
            ctx.result("Bad request - missing query parameter");
            return;
        }

        ChatApi.Message[] messages = new ChatApi.Message[]{
                new ChatApi.Message("system", systemPrompt),
                new ChatApi.Message("user", query)
        };

        ChatApi.Message message = Main.getChatApi().requestCompletion(messages)[0];

        json(ctx, JsonNode.object().set("answer", message.getContent()));
    }
}
