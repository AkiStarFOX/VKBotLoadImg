import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.responses.GetResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int USER_ID = 6502666;
    private static final String TOKEN = "733dfe5e733dfe5e733dfe5e6a735ec7547733d733dfe5e2824c85a408c46387176e199";
    private final static Random random = new Random();
    private static int offset = 0;

    public static void main(String[] args) {
        HttpTransportClient client = new HttpTransportClient();
        VkApiClient apiClient = new VkApiClient(client);
        UserActor actor = new UserActor(USER_ID, TOKEN);

        while (true) {
            List<Photo> list = getPhotoFromVK(offset, apiClient, actor);
            downloadImg(list);
            offset+=100;
        }

    }

    public static List<Photo> getPhotoFromVK(int offset, VkApiClient vk, UserActor actor) {
        GetResponse getAllResponse = null;
        try {
            getAllResponse = vk.photos().get(actor)
                    .ownerId(-104375368)
                    .albumId("wall")
                    .rev(false)
                    .count(100)
                    .offset(offset)
                    .execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return getAllResponse.getItems();
    }

    public static void downloadImg(List<Photo> list) {
        File file = null;
        BufferedImage img = null;
        boolean isLoaded = true;
        boolean is1280 = false;
        try {

            for (int i = 0; i < list.size(); i++) {
                String fileName = "C:\\Users\\Staff2\\Desktop\\PhotoForBot\\photo-104375368_" + list.get(i).getId() + ".png";
                if (list.get(i).getPhoto1280() != null) {
                    img = ImageIO.read(new URL(list.get(i).getPhoto1280()));
                    if (isLoaded) {
                        file = new File(fileName);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        ImageIO.write(img, "png", file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
