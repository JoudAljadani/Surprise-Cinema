public class ValidationManager {

        // Check empty sign up fields
        public static boolean isSignUpEmpty(String name, String email, String password, String age) {
            return name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty();
        }

        // Check empty sign in fields
        public static boolean isSignInEmpty(String email, String password) {
            return email.isEmpty() || password.isEmpty();
        }

        // Check email format
        public static boolean isValidEmail(String email) {
            return email.toLowerCase().endsWith("@gmail.com");
        }

        // Check password length
        public static boolean isValidPassword(String password) {
            return password.length() > 8;
        }

        // Check if age is a number
        public static boolean isValidAge(String age) {
            try {
                Integer.parseInt(age);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Convert age to int
        public static int convertAge(String age) {
            return Integer.parseInt(age);
        }
    }
