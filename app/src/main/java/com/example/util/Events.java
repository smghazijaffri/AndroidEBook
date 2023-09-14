package com.example.util;

public class Events {


    //Event used to update profile
    public static class ProfileUpdate {

        private String image,name,phone;

        public ProfileUpdate(String image,String name,String phone) {
            this.image = image;
            this.name=name;
            this.phone=phone;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPhone() {
            return phone;
        }
    }

    //Event used to update remove and update image
    public static class ProImage {

        private String string, imagePath;
        private boolean isProfile;

        public ProImage(String string, String imagePath, boolean isProfile) {
            this.string = string;
            this.imagePath = imagePath;
            this.isProfile = isProfile;
        }

        public String getString() {
            return string;
        }

        public String getImagePath() {
            return imagePath;
        }

        public boolean isProfile() {
            return isProfile;
        }

    }

    public static class FavBook {

        private String BookId;
        private String isFav;

        public String getBookId() {
            return BookId;
        }

        public void setBookId(String bookId) {
            BookId = bookId;
        }

        public String getIsFav() {
            return isFav;
        }

        public void setIsFav(String isFav) {
            this.isFav = isFav;
        }


    }

}
