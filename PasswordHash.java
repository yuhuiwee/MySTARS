import java.util.HashMap;

import com.lambdaworks.crypto.SCryptUtil;
import java.io.*;

public class PasswordHash {

    private static HashMap<String, String> userpassword;

    public PasswordHash() throws UserAlreadyExists {
        userpassword = new HashMap<String, String>();
    }

    public static void saveHashMap() {
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
    private static void loadHashMap() throws UserAlreadyExists {
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
            new PersonList();
        } catch (ClassNotFoundException c) {
            // System.out.println("Class not found");
            // c.printStackTrace();
            new PersonList();
        }
    }

    public static boolean checkPwd(String username, String pwd) throws UserNotFound, UserAlreadyExists {
        if (userpassword == null) {
            loadHashMap();
        }

        if (!userpassword.containsKey(username.toLowerCase())) {
            throw new UserNotFound("User Account not found!");
        } else {
            return SCryptUtil.check(pwd, userpassword.get(username.toLowerCase()));
        }
    }

    public static void addUserPwd(String username, String pwd) throws UserAlreadyExists {
        if (userpassword == null){
            loadHashMap();
        }

        userpassword.put(username.toLowerCase(), SCryptUtil.scrypt(pwd, 16, 16, 16));
        saveHashMap();
    }

    public static void removeUser(String username) throws UserAlreadyExists, UserNotFound {
        if (userpassword == null) {
            loadHashMap();
        }

        if (!userpassword.containsKey(username.toLowerCase())) {
            throw new UserNotFound("User Account not found!");
        } else {
            userpassword.remove(username.toLowerCase());
            saveHashMap();
        }
    }

    public static void changePwd(String oldpwd, String newpwd, String username)
            throws UserNotFound, UserAlreadyExists, WrongPassword {
        if (userpassword == null) {
            loadHashMap();
        }
        if (checkPwd(username, oldpwd)) {
            userpassword.remove(username);
            addUserPwd(username, newpwd);
            saveHashMap();
        } else {
            throw new WrongPassword("Wrong Password!");
        }
    }

}
