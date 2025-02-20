package de.glowman554.crawler.core;

import de.glowman554.crawler.core.queue.AbstractQueue;
import de.glowman554.search.BaseDatabaseConnection;
import de.glowman554.search.DatabaseConfig;
import de.glowman554.search.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrawlerDatabaseConnection extends BaseDatabaseConnection {
    private final PageInserter inserter = new PageInserter();
    private final Queue queue = new Queue(inserter);


    public CrawlerDatabaseConnection(DatabaseConfig config) throws SQLException {
        super(config);
    }

    public CrawlerDatabaseConnection(BaseDatabaseConnection base) {
        super(base);
    }


    public IPageInserter getInserter() {
        return inserter;
    }

    public AbstractQueue getQueue() {
        return queue;
    }

    private class Queue extends AbstractQueue {


        public Queue(IPageInserter pageInserter) {
            super(pageInserter);
        }

        @Override
        protected void push(@NotNull String url) {
            if (url.length() > 512) {
                Logger.log("URL is too long: " + url);
                return;
            }

            tryExecuteSilent("insert into `crawlerQueue` (`link`) values (?)", statement -> {
                statement.setString(1, url);
                statement.execute();
            });
        }

        private void deleteFromQueue(String link) {
            tryExecute("delete from `crawlerQueue` where `link` = ?", statement -> {
                statement.setString(1, link);
                statement.execute();
            });
        }

        @Nullable
        @Override
        public String pop() {
            synchronized (this) {
                String link = tryExecute("select `link` from `crawlerQueue` order by `timestamp` limit 1", PreparedStatement::execute, resultSet -> {
                    if (!resultSet.next()) {
                        return null;
                    }
                    return resultSet.getString("link");
                });

                if (link == null) {
                    return null;
                }

                deleteFromQueue(link);
                return link;
            }
        }

        @Override
        public void clearWithPrefix(String prefix) {
            tryExecute("delete from `crawlerQueue` where `link` like ?", statement -> {
                statement.setString(1, prefix + "%");
                statement.execute();
            });
        }
    }

    private class PageInserter implements IPageInserter {
        @Override
        public void insert(String url, String title, String text, String description, String keywords, String shortText) {
            tryExecute("insert into `webPages` (`link`, `title`, `content`, `description`, `keywords`, `shortText`) values (?, ?, ?, ?, ?, ?)", statement -> {
                statement.setString(1, url);
                statement.setString(2, title);
                statement.setString(3, text);
                statement.setString(4, description);
                statement.setString(5, keywords);
                statement.setString(6, shortText);
                statement.execute();
            });
        }

        @Override
        public void update(String url, String title, String content, String description, String keywords, String shortText) {
            tryExecute("update `webPages` set `title` = ?, `content` = ?, `description` = ?, `keywords` = ?, `shortText` = ?, `timestamp` = CURRENT_TIMESTAMP where `link` = ?", statement -> {
                statement.setString(1, title);
                statement.setString(2, content);
                statement.setString(3, description);
                statement.setString(4, keywords);
                statement.setString(5, shortText);
                statement.setString(6, url);
                statement.execute();
            });
        }

        @Override
        public void delete(String url) {
            tryExecute("delete from `webPages` where `link` = ?", statement -> {
                statement.setString(1, url);
                statement.execute();
            });
        }

        @Override
        public boolean test(String url) {
            Integer count = tryExecute("select count(*) from `webPages` where `link` = ?", statement -> {
                statement.setString(1, url);
                statement.execute();
            }, resultSet -> {
                resultSet.next();
                return resultSet.getInt(1);
            });

            return count != null && count > 0;
        }

        @Override
        public String fetchOldest() {
            return tryExecute("select `link` from `webPages` order by `timestamp` limit 1", PreparedStatement::execute, resultSet -> {
                if (!resultSet.next()) {
                    return null;
                }
                return resultSet.getString("link");
            });

        }
    }
}
