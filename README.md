# Community Support Resource Hub

## Team Members
- Matthew   
- Kyla  
- Nicholas  
- Yostina  

---

## Overview

The **Community Support Resource Hub** is a Java console application that helps users explore different types of financial and community support resources.

The program focuses on helping users find information about:

- financial assistance
- housing support
- food assistance

The goal of this program is to provide a simple way for users to estimate possible aid eligibility and access helpful resource links.

---

## Features

### Eligibility Calculator

The program includes an eligibility calculator that estimates how much financial aid a user might qualify for.

The user enters:

- number of independents (tax-paying adults)
- number of dependents
- annual household income

The program then calculates an estimated monthly income per person and compares it with an estimated cost of living. Based on this information, the program estimates whether the user may qualify for financial aid.

Input validation is implemented using **while loops** to prevent invalid values such as negative numbers.

---

### Resource Links

After calculating eligibility, the program provides resource links that may be helpful to the user.

These include resources related to:

- food assistance
- housing support
- educational funding

The program selects which links to show using conditional logic based on the user's estimated financial situation.

---

### Emergency Contacts

The program allows users to store emergency contacts.

Users can:

- add multiple contacts
- store names and phone numbers
- review their saved contacts

Phone numbers are validated using a helper method to ensure the format is acceptable.

---

### Aid Application Simulation

The program allows users to simulate applying for different aid programs.

Available options include:

- FAFSA
- Seattle Rental Housing Program
- Basic Food assistance

Users can apply for multiple programs, but the program prevents them from applying for the same one twice. Once all three programs are selected, the program stops asking for additional applications.

---

### Summary Page

The summary page shows a full overview of the user’s session.

This may include:

- household information
- estimated financial aid eligibility
- saved emergency contacts
- selected resource links
- aid programs the user simulated applying for

---

## Technologies Used

- Java
- Scanner (user input)
- while loops
- if / else statements
- arrays
- methods for organizing the code

---

## How to Run the Program

1. Open the project in a Java IDE (such as Eclipse).
2. Run the **HelloJava** main file.
3. Follow the menu prompts in the console.
