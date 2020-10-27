import java.util.HashMap;

import com.lambdaworks.crypto.SCryptUtil;
import java.io.*;

public class PasswordHash {

    private static HashMap<String, String> userpassword;

    public PasswordHash() throws UserAlreadyExists {
        userpassword = new HashMap<String, String>();
        addUserPwd("TestStudent1", "Password123");
        addUserPwd("TestStudent2", "Password123");
        addUserPwd("TestStudent3", "Password123");
        addUserPwd("TestStudent4", "Password123");
        addUserPwd("TestStudent5", "Password123");
        addUserPwd("TestStudent6", "Password123");
        addUserPwd("TestStudent7", "Password123");
        addUserPwd("TestStudent8", "Password123");
        addUserPwd("TestStudent9", "Password123");
        addUserPwd("TestStudent10", "Password123");
        addUserPwd("TestStudent11", "Password123");
        addUserPwd("TestStudent12", "Password123");
        addUserPwd("TestStudent13", "Password123");
        addUserPwd("TestStudent14", "Password123");
        addUserPwd("TestStudent15", "Password123");
        addUserPwd("Admin1", "Password123");
        addUserPwd("Admin2", "Password123");

        savehashmap();
    }

    private static void savehashmap() {
        try {
            FileOutputStream fos = new FileOutputStream("HashedPwd.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userpassword);
            oos.close();
            fos.close();
            return;
        }

        catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadhashmap() throws UserAlreadyExists {
        try {
            FileInputStream fis = new FileInputStream("HashedPwd.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object temp = ois.readObject();
            if (temp instanceof HashMap<?, ?>) {
                userpassword = (HashMap<String, String>) temp;
            }
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            // ioe.printStackTrace();
            new PasswordHash(); // If file not found, create default values
            savehashmap();
        } catch (ClassNotFoundException c) {
            // System.out.println("Class not found");
            // c.printStackTrace();
            new PasswordHash();
            savehashmap();
        }
    }

    public static boolean checkPwd(String username, String pwd) throws UserNotFound, UserAlreadyExists {
        if (userpassword == null) {
            loadhashmap();
        }

        if (!userpassword.containsKey(username.toLowerCase())) {
            throw new UserNotFound("User Account not found!");
        } else {
            return SCryptUtil.check(pwd, userpassword.get(username.toLowerCase()));
        }
    }

    public static void addUserPwd(String username, String pwd) throws UserAlreadyExists {
        if (userpassword != null & userpassword.containsKey(username.toLowerCase())) {
            throw new UserAlreadyExists("Username already taken!");
        }

        userpassword.put(username.toLowerCase(), SCryptUtil.scrypt(pwd, 16, 16, 16));
        savehashmap();
    }

    public static void removeUser(String username) throws UserAlreadyExists, UserNotFound {
        if (userpassword == null) {
            loadhashmap();
        }

        if (!userpassword.containsKey(username.toLowerCase())) {
            throw new UserNotFound("User Account not found!");
        } else {
            userpassword.remove(username.toLowerCase());
            savehashmap();
        }
    }

    public static void changePwd(String oldpwd, String newpwd, String username)
            throws UserNotFound, UserAlreadyExists, WrongPassword {
        if (userpassword == null) {
            loadhashmap();
        }
        if (checkPwd(username, oldpwd)) {
            userpassword.remove(username);
            addUserPwd(username, newpwd);
            savehashmap();
        } else {
            throw new WrongPassword("Wrong Password!");
        }
    }

}
