# INF124 Project Assignment #2

## JustPlants
JustPlants is an e-commerce website that sells indoor plants to customers and encourage people to connect with the environment.  

## Contributors
- Rongbing Lai
- Cindy Park
- Jiahao Lei

## Introduction to the General Layout
**Main page**\
The main page is built with Servlet, where all the products in page are fetched from the database that stores all plants in stock. The user can click each plants and the website will direct the customer to the product page. 

The main page is the product page where all the products are listed. There is a navigation bar with a Home tab that brings the user back to the main page, an About Company tab that shows the overview of the company business, and a Make Order tab that allows users to make orders by filling the form.  
The user can also access to the About Company page in the bottom bar. The bottom bar also includes the group members' names.\

**About Company page**\
The About Company page has the mission statement, and the about us, which is a business overview, and finally the team with team profiles and titles. Clicking on the title on the top center can navigate to the main page.\

**Detailed Product page**\
The product page is also built with servlet and SQL. By clicking on the products from the main page, it will lead you to each of the detailed product pages. The user can always navigate back to the main page or the about company page via the top navigation bar.The detailed product page has a bigger image on the left side. On the right side, there are the type of the products, the product name, description, size, instruction for caring, price and quantity modifying input and Add to cart button. By clicking on the Add to cart button, it will add the desired quantity of that product to customer's cart by storing all the information distinct from each HTTP sessions.\

**View Shopping Cart page**\
When the user switches to this page, they can see the item and quantity of the items they put in their cart. In this page, you can update quantities for each of the items and proceed to order. The view of the items in cart used both sql and session attribute. By matching the product id with the session's array attribute, it allows to selectively fetch product info from the database. When user updates quantities, it is reflected on "total price" section of the page.

**Order Form page**\
The order form is for customers to order products by filling in the information. The form has three sections. The first section is a cart, which allows customers to choose a product from the drop down box and enter the quantity. The second section is shipping information, asking customers to fill in the shipping info including names, phone number, address and shipping method. The third section is payment information where customers need to enter their credit card. Customers can reset the info by clicking on the reset button, or send the info by clicking on the send button to make an email.\

## Requirements
#1 is shown on the About Company page\
#2-#4, #9, #11 are shown on the Main page\
#5 is shown on the detailed product page\
#6-#8 are presented in the form page from each detailed product page\
#10 is shown in all pages\

## Credits
mainpage.css line 36-48\
https://stackoverflow.com/questions/643879/css-to-make-html-page-footer-stay-at-bottom-of-the-page-with-a-minimum-height-b/25218797#25218797


