import java.util.HashMap;
import com.lambdaworks.crypto.SCryptUtil;
import java.io.*;

/**
 * Represents the hashing of password for the users
 * @author Group 02
 * @version 1.0
 * @since 24 November 2020
 */
public class PasswordHash {

    /**
	 * Creates a string to string hash map for the users' passwords
	 */
	private static HashMap<String, String> userpassword;

    /**
     * Creates a string hash map
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     */
	public PasswordHash() throws UserAlreadyExists {
		userpassword = new HashMap<String, String>();
	}

    /**
     * Saves the hash map and push the updated hashed passwords into a file
     */
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

    /**
     * Loads the hash map and push the hashed passwords into a file
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     */
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

    /**
     * Check if the password entered belongs to a user
     * @param username This is the user's username
     * @param pwd This is user's password
     * @return This is a true/false on whether the user is successful in changing their password
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     */
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

    /**
     * Adds the password to a user
     * @param username This is the user's username
     * @param pwd This is user's password
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     */
	public static void addUserPwd(String username, String pwd) throws UserAlreadyExists {
		if (userpassword == null) {
			loadHashMap();
		}

		userpassword.put(username.toLowerCase(), SCryptUtil.scrypt(pwd, 16, 16, 16));
		saveHashMap();
	}

	/**
     * Removing a user from the system
     * @param username This is the user's username
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws UserNotFound This exception is thrown when the username entered is not found
     */
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

	/**
     * Changes the old password to a new password
     * @param oldpwd This is the user's old password
     * @param newpwd This is the user's new password
     * @param username This the user's username
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws WrongPassword This exception is thrown when wrong password is entered by the user
     */
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
