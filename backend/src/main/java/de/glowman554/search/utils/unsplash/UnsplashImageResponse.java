package de.glowman554.search.utils.unsplash;

import de.glowman554.config.Savable;
import de.glowman554.config.auto.AutoSavable;
import de.glowman554.config.auto.Saved;

public class UnsplashImageResponse extends AutoSavable {
    @Saved(remap = Savable.class)
    private UnsplashResponseUrl urls = new UnsplashResponseUrl();
    @Saved(remap = Savable.class)
    private UnsplashResponseUser user = new UnsplashResponseUser();

    public UnsplashResponseUrl getUrls() {
        return urls;
    }

    public UnsplashResponseUser getUser() {
        return user;
    }

    public static class UnsplashResponseUser extends AutoSavable {
        @Saved
        private String username;
        @Saved
        private String name;

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }
    }

    public static class UnsplashResponseUrl extends AutoSavable {
        @Saved
        private String raw;
        @Saved
        private String full;
        @Saved
        private String regular;
        @Saved
        private String small;
        @Saved
        private String thumb;

        public String getRaw() {
            return raw;
        }

        public String getFull() {
            return full;
        }

        public String getRegular() {
            return regular;
        }

        public String getSmall() {
            return small;
        }

        public String getThumb() {
            return thumb;
        }
    }
}
