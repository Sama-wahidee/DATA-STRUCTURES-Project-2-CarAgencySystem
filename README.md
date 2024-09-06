# 🚗 Car Agency Application

**Car Agency Application** is a system designed to manage car inventory, process customer orders, and maintain records of sold cars.

## 📝 Note

**This project was awarded a full mark by Dr. Iyad Jaber <3.**

## 🔧 Data Structures

This project implements a car inventory management system using a combination of data structures to efficiently store and access car data. The overall data structure includes:

- **Double Linked List of Car Brands:**
  - Each brand node contains a **Single Linked List** of cars sorted by year.
  - Cars are sorted alphabetically by brand and within each brand by the year of manufacture.

- **Queues:**
  - Customer orders are processed using a queue (First Come, First Served).
  
- **Stacks:**
  - Sold cars are stored in a stack for easy access to the most recently sold cars.

## 💻 Main Interface

The main interface allows administrators to manage car inventory, process customer orders, and track sold cars.

![Main Interface](SecondProject_Cars/src/resources/1.jpg)

## 🚨 Data Upload Notification

Notifications indicate successful data upload and any issues encountered during processing.

![Upload Notification](SecondProject_Cars/src/resources/2.jpg)

## 🚘 Car Inventory Management

Administrators can:

- Add new car brands and models.
- Update or delete existing car records.
- Search for specific cars using filters such as year, color, model, or price.

![Car Inventory Management](SecondProject_Cars/src/resources/3.jpg)

![Car Inventory Management](SecondProject_Cars/src/resources/4.jpg)

![Customer Order Management](SecondProject_Cars/src/resources/9.jpg)

![Customer Order Management](SecondProject_Cars/src/resources/5.jpg)


## 📋 Customer Order Management

Customers can browse available cars, apply filters, and place orders.

![Order Processing](SecondProject_Cars/src/resources/6.jpg)

![Additional Features](SecondProject_Cars/src/resources/7.jpg)

![Statistics](SecondProject_Cars/src/resources/8.jpg)

![Statistics](SecondProject_Cars/src/resources/11.jpg)


## 📊 Order Processing

The admin can process customer orders, moving completed orders to a stack of finished orders. If a car is unavailable, the order can be postponed.

![Statistics](SecondProject_Cars/src/resources/10.jpg)


## 📈 Statistics
- **Reporting:** Generate reports on the last 10 sold cars.
![Customer Feedback](SecondProject_Cars/src/resources/12.jpg)


## 💾 Save Order File

Administrators can save processed orders to a file for future reference.

![Save Order File](SecondProject_Cars/src/resources/13.jpg)

