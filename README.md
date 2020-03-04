# Bulk Email Distribution

## Main Idea

To solve this problem I used multi thread `EmailSender` to send emails. Email address are provided by `EmailListReader`	through BlockingQueue.  Besides, monitor thread is used to periodically provide current sending status in the console.

## Project Structure

* `com.github.diskun00.App`

  program entry

* `com.github.diskun00.EmailBean`

  basic email information

* `com.github.diskun00.EmailListReader`

  read email list and save to BlockingQueue

* `com.github.diskun00.EmailSender`

  sending emails

* `com.github.diskun00.Monitor`

  periodically report the email sending status

