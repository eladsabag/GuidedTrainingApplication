# GuidedTrainingApplication
An android application that provides the ability to have a guided training anywhere anytime. The application handles different users and each user can customize his own trainings or use the built in trainings. Every training record can be saved and shown in the history page. The main purpose is having a training without expensive gym equipment and at any location that the user desires.

The first page display suggestions to login or register with many option such as mannually, google, phone:

![image](https://user-images.githubusercontent.com/68230346/176455569-27e82ef1-45d2-40a3-b5c9-2aaba761b571.png)

Clicking on one of the icons in the bottom of the page will display a registration with exist details:

![image](https://user-images.githubusercontent.com/68230346/176455962-aee4ea31-898e-4a2b-80d5-07e2cc6b6ae8.png)

Now clicking on one of the options will lead to external registration process, for example clicking on google:

![image](https://user-images.githubusercontent.com/68230346/176456143-6f80476c-f295-4349-89a3-89e248defb77.png)

Now google will authenticate the user in their server and if there is a match, then our application will retrieve the data and sign up the user.

There is more options of registration, for example clicking on join now in the first page will lead to registration page:

![image](https://user-images.githubusercontent.com/68230346/176456507-5a27abbe-e96c-4d1d-ac4b-2179fae8ad68.png)

Or if you already have an account you can just clicking the login button in the first page and authenticate yourself:

![image](https://user-images.githubusercontent.com/68230346/176456736-2338f7d3-8a1a-41a3-ab43-793e59cea968.png)

After successfull sign up/in, we can start using the application. The main page of the application shows a quick start button for the convinience of the users:

![image](https://user-images.githubusercontent.com/68230346/176457064-871680a6-ed22-412b-8235-b15821d74b47.png)

Tapping quick start will generate a random training from the built in trainings that the application provides, and start the active training activity.
But before that, we can also customize our trainings under 'Customize' bar:

![image](https://user-images.githubusercontent.com/68230346/176458174-c1390732-e011-43c6-8f45-c3ea8aa629e5.png)

After building our training, if all the fields were filled properly, then the training will be displayed under 'Trainings' along with the built in trainings:

![image](https://user-images.githubusercontent.com/68230346/176458441-6987fb7f-d826-48b9-ac5a-2a6531b76ae6.png)

![image](https://user-images.githubusercontent.com/68230346/176458302-27e2d99c-a03c-469c-a024-01b9907207d7.png)

Now starting one of the trainings will start the active training activity with the chosen training. For example the customized training:

![image](https://user-images.githubusercontent.com/68230346/176458976-2292535d-1dd6-4083-b2be-843d6f86cf70.png)

![image](https://user-images.githubusercontent.com/68230346/176458858-8a00f29e-fc5f-4971-b29e-ddcdcb3b05e4.png)

The active training activity display all of the videos attached to the chosen training.

After finishing a training, we wil be redirected to the 'History' activity where all of our trainings records being saved:

![image](https://user-images.githubusercontent.com/68230346/176459481-be2011a6-5358-4de0-99b5-04fac9345290.png)

If we choose to save the training then it will be shown on the screen immidetialy, and tapping the record will show the exact location of the training on map:

![image](https://user-images.githubusercontent.com/68230346/176459816-550c07b7-b7f3-41b5-9a50-5a86bd55f493.png)

There is option to edit settings under 'Settings' such as vibration, audio, or logout. This page also shows the user details and initials(of the user email) image the we get from the dicebear API:

![image](https://user-images.githubusercontent.com/68230346/176460267-5bee9c2e-2c56-432a-8175-9831552683b0.png)

Tapping logout will pop up a question if we sure and if we tap yes then it redirect us to the authentication page:

![image](https://user-images.githubusercontent.com/68230346/176460422-8576d3e1-dc05-4ea1-98d3-b6382733564a.png)

![image](https://user-images.githubusercontent.com/68230346/176460497-4b415fb7-5a75-4dd6-b724-2264132d3011.png)



