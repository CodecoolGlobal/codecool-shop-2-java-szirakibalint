# Codecool shop (sprint 2)

## Story

It's time to enhance the Online Shop, an online eCommerce web-application with Java,
where users can not only browse products, add them into a Shopping Cart,
checkout items and make payments, but can also log in and see the abandoned shopping cart or their order history.

> Did you know that the very first product on eBay was a broken laser pointer?
> If you don't believe, check their history: [eBay history](https://www.ebayinc.com/company/our-history/)

## What are you going to learn?

- Log properly.
- Use property files for separate project settings.
- Write tests and mock out dependencies to ensure the correct functionality and gain confidence at future  modification.
- Using databases to make the data persistent.
- Use the `DAO` design pattern in `Java`.
- Refresh your SQL knowledge.


## Tasks

1. Create a new sprint tab on the existing backlog. Last week, you had a long list of stories, a few new stories this week.
    - The new items are added to the backlog.
    - The team has created a sprint 2 plan, based on the unified backlog.

2. As you work in a new repository but still need the code from the previous sprint, add the `codecool-shop-2` repository as a new remote to the repository of the previous sprint, then pull (merge) and push your changes into it.
    - There is a merge commit in the project repository that contains code from the previous sprint.

3. As a Developer, I want to cover the `ProductService` and any other Service objects with unit tests, so that I can safely change the implementation later.
    - All methods of the Services are tested, independently from their DAO dependencies, using mocking.
    - Both "happy-paths" and "sad-paths" are tested.
    - At least eleven unit tests pass.

4. As a Developer, I want to have proper log messages when any failure happens to make it possible to find any malfunction in the system and track back user complaints.
    - With the dependency management tool, a logging facade is put under the app. Maven is used for adding SLF4J to the application as a dependency.
    - All exceptions are logged with a proper description message.

5. As an Operator of the Shop, I want the product data to be persistent, so that I can restart the application without losing product data.
    - There is an empty PostgreSQL database, named `codecoolshop`.
    - There is an initializer script file, `src/main/sql/init_db.sql`. When runing the script file, all empty tables are created that will store `Products`, `ProductCategories`, and `Suppliers`.
    - When the page is loaded and DB is used, suppliers, product categories, and products are loaded from the database using `ProductDao`, `ProductCategoryDao`, and `SupplierDao`.

6. As a Developer, I want to read the DAO settings and DB connection parameters (url, database name, usr, pwd) from a config file, so I can change the settings of the application on every environment without compiling again.
    - The config file `src/main/resources/connection.properties`
has the following structure (exact values may vary).
`url: localhost:5432
      database: codecoolshop
      user: postgres
      password: postgres
      dao: memory`
    - The application reads the dao type settings from the property file. When the setting is `memory`, memory dao implementations are used. When the setting is `jdbc`, JDBC dao implementations are used.
    - The application reads the DB settings from the property file. The `JDBC Connection` is initialized based on these settings.

7. As an Operator of the Shop, I want to keep Order data safe and persistent, so that I do not lose money due to technical issues.
    - If the user starts a checkout process, all Order data is saved into the database at every step (except the cart).

8. As a User, I want to sign up (make a personal account), so that I can store Orders on my personal account.
    - There is a `Sign up` option on the website.
    - There is a signup form with the following fields.
- `name`
- `email`
- `password`
    - When the user submits the form with valid information, the system saves the data as a new `User`.
    - The system sends a welcome email upon successful registration.
    - When the user submits the form with invalid information, the program displays the form with the invalid data, and some error descriptions.

9. As a User, I want to able to log in, so that I can and access my personal data. I also want to be able to log out.
    - There is a `Login` option on the website.
    - When the user selects "Login", a login form is displayed with the following fields.
- email address
- password
    - When the user submits the form with valid information, they are authenticated and given a new logged-in session.
    - When the user submits the form with invalid information, an error message is displayed.
    - If the user is logged in, a Logout option is displayed on the webpage. When the user selects "Logout", the session is reset, and the user is redirected to the login form.

10. As a logged-in User, I want to see my Order history, with my previous Orders and their statuses.
    - There is an `Order history` option on the webpage.
    - A list of all Orders of the user is displayed, with the following details.
- order date
- order status (checked / paid / confirmed / shipped)
- total price
- product list (with product name, price)

11. As a logged-in User, I want to save the current items of my Shopping cart so that I can order my selected Products later.
    - There is a `Save my cart` button (on the Shopping cart review page).
    - When clicking this button, the system saves the cart items into the database for the logged-in User.
    - When a User with a previously saved shopping cart logs in the shopping cart is refilled with the saved items.

12. As a logged-in User, I want to save my billing and shipping info to my personal account, so that I don't need to retype the data every time during checkout.
    - There is a `Billing info` option on the webpage.
    - Clicking `Billing info` displays a form asking for all personal billing and shipping information required for checkout.
    - On the Shopping Cart review page, clicking the "Checkout" button as a logged-in user, the billing and shipping info is pre-filled on the checkout form.

## General requirements

- Advanced OOP concepts are used in the project, such as inheritance.
There is at least one abstract class.
There is at least one interface implemented.
- The page does not show a server error anytime during the review.
- All code is pushed to GitHub repository in atomic commits. The implemented feature-related commits are managed on separated feature branches, and merged in a pull request to the `master` branch.

## Hints

- It's not required to integrate real payment services - you can use fake payment implementations.
- Use the DAO implementations via interfaces so that it will be easy to change the implementation behind the interface.
- You can use `decimal(20,2)` sql type to store your decimal values in the database

## Background materials

- <i class="far fa-exclamation"></i> [Logging](project/curriculum/materials/pages/general/logging.md)
- <i class="far fa-exclamation"></i> [Logging with SLF4J](project/curriculum/materials/pages/java/logging-with-slf4j.md)
- <i class="far fa-exclamation"></i> [Mocking](project/curriculum/materials/pages/general/mocking.md)
- <i class="far fa-exclamation"></i> [Unit tests, stubs, mocks quick overview by Martin Fowler](https://www.youtube.com/watch?v=sEFhB71tmPM)
- <i class="far fa-exclamation"></i> [Java Dao pattern](https://www.baeldung.com/java-dao-pattern)
- <i class="far fa-exclamation"></i> [Introducing servlets](project/curriculum/materials/pages/java/introducing-servlets.md)
- <i class="far fa-exclamation"></i> [Servlet tutorial](https://www.tutorialspoint.com/servlets/servlets-form-data.htm)
- <i class="far fa-exclamation"></i> [Java properties](https://www.baeldung.com/java-properties)

