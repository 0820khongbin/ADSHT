package com.thur.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thur.demo.model.post.Post;
import com.thur.demo.model.post.PostComment;
import com.thur.demo.model.post.PostCommentRepository;
import com.thur.demo.model.post.SubCommentRepository;
import com.thur.demo.model.shopping.Shop;
import com.thur.demo.model.user.User;
import com.thur.demo.service.FriendService;
import com.thur.demo.service.PostCommentService;
import com.thur.demo.service.PostService;
import com.thur.demo.service.ShopService;
import com.thur.demo.service.SubCommentService;
import com.thur.demo.service.UserService;
import com.thur.demo.util.DefaultIcon;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/ajax/data-generater")
public class DataGeneraterController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    SubCommentService subCommentService;
    @Autowired
    FriendService friendService;
    @Autowired
    SubCommentRepository subCommentRepository;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    ShopService shopService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private DefaultIcon defaultIcon;

    List<String> user = new ArrayList<>();
    List<String> greeting = new ArrayList<>();
    List<String> shops = new ArrayList<>();
    List<String> shopPics = new ArrayList<>();
    List<String> products = new ArrayList<>();
    List<String> productPics = new ArrayList<>();
    List<String> postpic = new ArrayList<>();
    int r = 0, r1 = 0, r2 = 0, r3 = 0;

    @GetMapping("/GO")
    public String getMethodName(HttpSession session) {
        try {
            String userPath = "name";
            this.user = defaultIcon.nameRandomStringList(userPath);
            String greetingPath = "greeting";
            this.greeting = defaultIcon.nameRandomStringList(greetingPath);
            String shopsPath = "shop";
            this.shops = defaultIcon.nameRandomStringList(shopsPath);
            String shopPicsPath = "\\img\\postPic\\shop";
            this.shopPics = defaultIcon.picRandomStringList(shopPicsPath);
            String productsPath = "product";
            this.products = defaultIcon.nameRandomStringList(productsPath);
            String productPicsPath = "\\img\\postPic\\product";
            this.productPics = defaultIcon.picRandomStringList(productPicsPath);
            String postpicPath = "\\img\\postPic\\photo";
            this.postpic = defaultIcon.picRandomStringList(postpicPath);

            // userBuilder(session);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // postBuilder(session);
        // commentBuilder(session);
        shopMake(session);
        return "All done";
    }

    @GetMapping("/user")
    public String userBuilder(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        for (String s : user) {
            map.put("userName", s.split("-")[0].trim() + "@gmail.com");
            map.put("password", s.split("-")[0].trim());
            map.put("lastName", s.split("-")[1].trim());
            JSONObject data = new JSONObject(map);
            // System.out.println(data);
            userService.addUsers(data, session);
        }
        return "user Done";
    }

    @GetMapping("/post")
    public String postBuilder(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            // 亂數User
            r = (int) (Math.random() * user.size());
            // 亂數招呼
            r2 = (int) (Math.random() * greeting.size());

            map.put("userName", user.get(r).split("-")[0].trim() + "@gmail.com");
            map.put("password", user.get(r).split("-")[0].trim());
            map.put("content", greeting.get(r2));
            map.put("permission", String.valueOf("1"));
            try {
                map.put("image", defaultIcon.picRandom("img\\postPic\\photo\\", postpic));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error");
            }
            JSONObject data = new JSONObject(map);
            User user = userService.login(data, session);
            if (user != null) {
                postService.addPost(data, user, session);
            } else {
                System.out.println("user 錯誤");
            }

        }
        return "post done";
    }

    @GetMapping("friend")
    public String friendBuilder(HttpSession session) {

        for (int i = 0; i < user.size(); i++) {

        }

        return "friend done";
    }

    @GetMapping("/comment")
    public String commentBuilder(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < user.size(); i++) {
            r = (int) (Math.random() * 50);

            for (int j = 0; j < r; j++) {
                map.put("userName", user.get(r) + "@gmail.com");
                map.put("password", user.get(r));
                map.put("content", user.get(r) + " comment: " + j);
                JSONObject data = new JSONObject(map);
                User user = userService.login(data, session);

                r1 = (int) (Math.random() * postCommentRepository.count());
                Post post = postService.findOnePost(r1);
                postCommentService.addComment(data, post, user);
            }

        }
        return "comment done";
    }

    @GetMapping("/subComment")
    public String subCommentBuilder(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < user.size(); i++) {
            r = (int) (Math.random() * 20);
            for (int j = 0; j < r; j++) {
                map.put("userName", user.get(r) + "@gmail.com");
                map.put("password", user.get(r));
                map.put("content", user.get(r) + " subComment" + j);
                JSONObject data = new JSONObject(map);
                User user = userService.login(data, session);

                r1 = (int) (Math.random() * subCommentRepository.count());
                PostComment postComment = postCommentService.findOnePostCommentById(r1);
                subCommentService.addSubComment(data, postComment, user);
            }

        }
        return "subComment done";
    }

    @GetMapping("/shopMaker")
    public String shopMake(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        // 亂數User
        r = (int) (Math.random() * user.size());
        // 商店名
        r2 = (int) (Math.random() * shops.size());
        // map資料
        map.put("userName", user.get(r).split("-")[0].trim() + "@gmail.com");
        map.put("password", user.get(r).split("-")[0].trim());
        map.put("shopName", shops.get(r2).split("-")[0].trim());
        System.out.println(shops.get(r2));
        try {
            map.put("image", defaultIcon.picRandom("img\\postPic\\shop\\", shopPics));
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("description", shops.get(r2).split("-")[1].trim());
        // JSONObject
        JSONObject data = new JSONObject(map);

        User usera = userService.login(data, session);
        Shop shop = shopService.openNewShop(data, usera);

        productMake(shop, usera);
        return new String();
    }

    @GetMapping("/productMaker")
    public String productMake(Shop shop, User user) {
        Map<String, String> map = new HashMap<>();
        r = (int) (Math.random() * 10);
        for (int i = 0; i < r; i++) {
            r2 = (int) (Math.random() * products.size());
            map.put("description", products.get(r2));
            map.put("productName", products.get(r2));
            map.put("stock", "" + (int) (Math.random() * 99999));
            map.put("price", "" + (int) (Math.random() * 99999));
            try {
                map.put("image", defaultIcon.picRandom("img\\postPic\\product\\", productPics));
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject data = new JSONObject(map);
            shopService.addNewProduct(data, shop);
        }
        return new String();
    }

}
