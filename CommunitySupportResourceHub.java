package com.verification.test;

import java.util.Scanner;

public class CommunitySupportResourceHub { 
	// values used to determine links
	static int firstThreshold = 20000; // made up number--represents middle bound links
	static int secondThreshold = 100000; // made up number--represents upper bound links

	// values used in eligibility calculator and in aid links
	static int independents;// adults per household
	static int totalIncome;// total household income
	static int dependents;// children per household
	static double eligibility;
	static int aidAmount;
	static int applicationChoice;
	static String name;
	
	static Scanner scnr;
	
	// values used in contacts
	static String[] emergencyName;
	static String[] emergencyNum;

	// values that do cosmetic changes to the summary and menu based on whether the user has opened these options
	static boolean completedCalculator = false;
	static boolean completedResources = false;
	static boolean completedContacts = false;
	static boolean completedSummary = false;

	// values used to determine what to put on the summary.
	static boolean putAidOnSummary = false;
	static boolean putContactsOnSummary = false;
	static boolean[] linksSummary = { false, false, false };// food=0,rent=1,education=2

	// application simulation summary values
	static boolean appliedFafsa = false;
	static boolean appliedHousing = false;
	static boolean appliedFood = false;

	//these links were used for "proof of concept" purposes and may not reflect real eligibilitu.
	static String[] foodLinks = { "https://www.usa.gov/emergency-food-assistance", "https://www.dshs.wa.gov/esa/community-services-offices/basic-food",
			"https://www.feedingamerica.org/our-work/hunger-relief-programs/snap" };
	static String[] rentLinks = { "https://www.seattle.gov/rentinginseattle/renters/i-need-help", "https://home.treasury.gov/policy-issues/coronavirus/assistance-for-state-local-and-tribal-governments/emergency-rental-assistance-program",
			"https://usafundingapplications.org/v9/registration-form-060325/?category=real_estate" };
	static String[] educationLinks = { "https://studentaid.gov/h/apply-for-aid/fafsa", "https://wsac.wa.gov/sfa-overview",
			"https://usafundingapplications.org/v9/registration-form-060325/?category=education"};

	public static void main(String[] args) {
		scnr = new Scanner(System.in);

		System.out.println("=====================================");
		System.out.println("   Community Support Resource Hub");
		System.out.println("=====================================");

		System.out.print("Enter your name: ");
		name = scnr.nextLine(); // allows full names with spaces

		boolean running = true;

		while (running) {
			printMainMenu();

			System.out.print("Choose an option (1-6): ");
			String choice = scnr.nextLine().trim();

			switch (choice) {
			case "1":
				eligibilityCalculator();
				completedCalculator = true;
				break;

			case "2":
				nextSteps();
				completedResources = true;
				break;

			case "3":
				emergencyContacts();
				completedContacts = true;
				break;

			case "4":
				viewSummary();
				completedSummary = true;
				break;

			case "5":
				applyForSupport();
				break;

			case "6":
				running = false;
				System.out.println("\nGoodbye, " + name + "!");
				break;

			default:
				System.out.println("\nInvalid option. Please enter 1, 2, 3, 4, 5, or 6.\n");
			}
		}
	}

	//prints the main menu to the user
	private static void printMainMenu() {
		System.out.println();
		System.out.println("========== MAIN MENU ==========");
		if (completedCalculator) {
			System.out.println("1) Change Eligibility Information");
		} else {
			System.out.println("1) Use Eligibility Calculator");
		}
		System.out.println("2) See Resource Links");
		if (completedContacts) {
			System.out.println("3) Change Emergency Contacts");
		} else {
		    System.out.println("3) Set Emergency Contacts");
		}	
		if (completedSummary) {
			System.out.println("4) Revisit Summary");
		} else {
			System.out.println("4) View Summary");
		}
		System.out.println("5) Simulate Applying for Aid");
		System.out.println("6) Exit");
		System.out.println("===============================");
	}
	
	//does the actual calculations of the calculator
	public static void eligibilityCalc(int adults, int totalAnnualIncome, int children) {
		// adults = independents
		// children = dependents
		if (children + adults == 0) {
			aidAmount = 0;
			return; // No one is in your household, how could you qualify for aid? Also avoids divide by 0 error.
		}

		int incomePerPerson = ((totalAnnualIncome / 12) / (adults + children));// calculate monthly income
		aidAmount = 0;
		int costOfLiving = 0;

		// calculate aid based on monthly income
		if (children > 0) {
			// normal calculations
			if (adults > 0) {
				costOfLiving = 2700 * (adults + children);
				aidAmount = costOfLiving - (incomePerPerson * (adults + children));

				if (aidAmount < 0) {
					// not eligible for financial aid
					aidAmount = 0;
				} // end
			} // end
			else if (adults == 0) {
				// children only situation
				costOfLiving = 3500 * (1 + children); // minimum 1 parent
				aidAmount = costOfLiving - (incomePerPerson * children);

				if (aidAmount < 0) {
					// not eligible for financial aid
					aidAmount = 0;
				} // end
			} // end
		} // end if children > 0

		else if (children == 0) {
			// no dependents calculations
			costOfLiving = 5000 * adults;
			aidAmount = costOfLiving - (incomePerPerson * adults);

			if (aidAmount < 0) {
				// not eligible for financial aid
				aidAmount = 0;
			} // end
		} // end else if

		return;
	}// END METHOD

	//prints aid info based on the calculator
	public static void printAidInfo() {
		if (aidAmount > 0) {
			System.out.println("You are potentially eligible for $" + aidAmount + " of financial aid per/month!");
		} else {
			System.out.println("You are ineligible for financial aid at this time.");
			System.out.println("If you would like to re-adjust your inputs, please retry from the main menu.");
		}
	}

	//runs the eligibility calculator for the user
	public static void eligibilityCalculator() {
		System.out.println("\n[Eligibility + Resource Links]");

		System.out.println("Welcome to the financial aid eligibility calculator!");
		System.out.println(
				"Calculations are based on Seattle area, and are not 100% accurate or determinate of real financial aid.");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println();

		System.out.print("Enter the number of independants/taxpayers in your household: ");
		independents = scnr.nextInt();// get number of independents in household

		while (independents < 0) {
			System.out.println("||ERROR|| Number of independents/taxpayers cannot be less than 0, please try again.");

			System.out.println();// indent

			System.out.println("Enter the number of independents/taxpayers in your household: ");
			independents = scnr.nextInt(); // get number of independents if 1st time failed
		}

		System.out.println();// indent

		System.out.print("Enter the number of dependents/non-taxpayers in your household: ");
		dependents = scnr.nextInt(); // get number of dependents

		while (dependents < 0) {
			System.out.println("||ERROR|| Number of dependents/non-taxpayers cannot be less than 0, please try again.");

			System.out.println();// indent

			System.out.print("Enter the number of dependents/non-taxpayers in your household: ");
			dependents = scnr.nextInt(); // get number of dependents if 1st time failed
		}

		System.out.println();// indent

		System.out.print("Enter the total annual household income: ");
		totalIncome = scnr.nextInt();// get total annual household income

		while (totalIncome < 0) {
			System.out.println("||ERROR|| Total annual household income cannot be less than 0. Try again.");

			System.out.println();// indent

			System.out.print("Enter the total annual household income: ");
			totalIncome = scnr.nextInt();
		}

		scnr.nextLine();
		System.out.println();// indent
		eligibilityCalc(independents, totalIncome, dependents);
		printAidInfo();

		System.out.println("Would you like this information on your summary? (Y/N)");
		putAidOnSummary = yesNoCheck();

		pressEnterToContinue();
	}

	//method that determines if a phone number is valid-- not super strict
	private static boolean isValidPhoneNumber(String phoneNumber) {
		if (phoneNumber.contains(" ") || phoneNumber.contains(")") || phoneNumber.contains("(")
				|| phoneNumber.contains("_") // illegal characters
				|| (phoneNumber.contains("-") && phoneNumber.length() != 12) // 12 characters with dashes
				|| (!phoneNumber.contains("-") && phoneNumber.length() != 10)) // 10 without dashes
		{
			return false;
		}

		return true;
	}

	// prints the emergency contacts that the user specified
	private static void printContacts() {
		System.out.println("~~~~~~~~~~~~~~~~~~~");
		for (int j = 0; j < emergencyName.length; j++) {
			System.out.println("Emergency Contact " + (j + 1) + ": ");
			System.out.println("Name: " + emergencyName[j]);
			System.out.println("Phone Number: " + emergencyNum[j]);
			System.out.println("~~~~~~~~~~~~~~~~~~~");
		}
	}

	//user sets emergency contacts
	private static void emergencyContacts() {
		System.out.println("\n[Emergency Contacts]");

		System.out.print("Enter the number of emergency contacts you'd like to create:");
		// get array size
		int arraySize = scnr.nextInt();

		while (arraySize < 1) {
			System.out.println("||ERROR|| You must enter at least one emergency contact. Try again.");
			System.out.println("Enter the number of emergency contacts you'd like to create: ");
			arraySize = scnr.nextInt();
		}

		scnr.nextLine();
		System.out.println();// indent

		// declare string array
		emergencyName = new String[arraySize];
		emergencyNum = new String[arraySize];

		// populate emergencyName array with contact names
		for (int i = 0; i < emergencyName.length; ++i) {
			System.out.print("Enter emergency contact name " + (i + 1) + ": ");
			emergencyName[i] = scnr.nextLine();
		} // end for

		// populate emergencyNum array with contact phone numbers
		for (int k = 0; k < emergencyNum.length; ++k) {
			System.out.println();// indent
			System.out.print("Enter " + emergencyName[k] + "'s phone number: ");
			emergencyNum[k] = scnr.nextLine();

			// ensure that the phone number is valid
			while (!isValidPhoneNumber(emergencyNum[k])) {
				System.out.println();
				System.out.println(
						"||Error|| Phone number cannot contain spaces, parentheses, or underscores. Must be 12 characters with dashes, or 10 characters without dashes. ");
				System.out.print("Enter " + emergencyName[k] + "'s phone number: ");
				emergencyNum[k] = scnr.nextLine();
			} // end while
		} // end for

		System.out.println();// indent

		// evaluate

		System.out.print("Would you like to review your emergency contacts? (Y/N) ");

		// print emergency contact info if user wants it
		if (yesNoCheck()) {
			System.out.println("Yes! Emergency Contact Review:");
			printContacts();
			System.out.println();// indent
			System.out.println("Review finished.");
		}

		System.out.print("Would you like to add this to the summary? (Y/N) ");
		putContactsOnSummary = yesNoCheck();

		pressEnterToContinue();
	}

	//prints "next step" financial resource links to the user
	private static void nextSteps() {
		System.out.println("\n[Next Steps / Resources]");
		
		if (!completedCalculator) {
			System.out.println("\n No eligibility information found! Please use the Eligibility Calculator and return.\n");
			pressEnterToContinue();
			
			return;
		}

		eligibility = totalIncome / (double)(dependents + independents);

		boolean getMoreInfo = true;
		while (getMoreInfo) {
			selections();

			System.out.println("Would you like more information in another area? (Y/N) ");
			getMoreInfo = yesNoCheck();
		}

		pressEnterToContinue();
	}

	//simulate applying for aid
	private static void applyForSupport() {

		System.out.println("\n[Application Simulation]");
		System.out.println("Welcome to the application simulation!");
		System.out.println();

		boolean applying = true;

		while (applying) {

			if (appliedFafsa && appliedHousing && appliedFood) {
				System.out.println("You have already applied for all available aid programs.");
				break;
			}

			System.out.println("1. FAFSA (academic scholarship)");
			System.out.println("2. Seattle Rental Housing Program (housing aid)");
			System.out.println("3. Basic Food (food assistance)");
			System.out.println("~~~~~~~~");
			System.out.println("Which aid would you like to simulate applying for? Enter selection of 1-3: ");

			applicationChoice = scnr.nextInt();

			while (applicationChoice != 1 && applicationChoice != 2 && applicationChoice != 3) {
				System.out.println("\nInvalid option. Please enter 1, 2, or 3\n");
				applicationChoice = scnr.nextInt();
			}

			if (applicationChoice == 1) {

				if (appliedFafsa) {
					System.out.println("You already applied for FAFSA.");
				} else {
					appliedFafsa = true;
					System.out.println("Awesome! You just applied for FAFSA (Free Application for Federal Student Aid).");
				}

			}

			else if (applicationChoice == 2) {

				if (appliedHousing) {
					System.out.println("You already applied to the Seattle Rental Housing Program.");
				} else {
					appliedHousing = true;
					System.out.println("Awesome! You just applied to the Seattle Rental Housing Program!");
				}

			}

			else if (applicationChoice == 3) {

				if (appliedFood) {
					System.out.println("You already applied to the Basic Food program.");
				} else {
					appliedFood = true;
					System.out.println("Awesome! You just applied to the Basic Food program!");
				}

			}

			scnr.nextLine();

			if (appliedFafsa && appliedHousing && appliedFood) {
				System.out.println("You have now applied for all available aid programs.");
				break;
			}

			System.out.println("Would you like to apply for another type of aid? (Y/N)");
			applying = yesNoCheck();
		}

		pressEnterToContinue();
	}

	//prints the summary to the user
	private static void viewSummary() {
		System.out.println("\n[View Summary]");

		System.out.println();
		System.out.println("========== USER SUMMARY ==========");
		System.out.println("Name: " + name);
		System.out.println();
		if (putAidOnSummary) {
			System.out.println("Household Information:");
			System.out.println("Independents: " + independents);
			System.out.println("Dependents: " + dependents);
			System.out.println("Total Household Income: $" + totalIncome);
			System.out.println();
			System.out.println("Estimated Financial Aid:");
			System.out.println("$" + aidAmount + " per month");
			System.out.println();
		}
		if (putContactsOnSummary) {
			System.out.println("Your Contacts: ");
			printContacts();
			System.out.println();
		}
		if (linksSummary[0] || linksSummary[1] || linksSummary[2]) {
			System.out.println("Here are your support links: ");
			if (linksSummary[0] == true) {
				System.out.println("Food: ");
				printLinks(foodLinks);
			}
			if (linksSummary[1] == true) {
				System.out.println("Rent: ");
				printLinks(rentLinks);
			}

			if (linksSummary[2] == true) {
				System.out.println("Education: ");
				printLinks(educationLinks);
			}
			System.out.println();
		}

		if (appliedFafsa || appliedHousing || appliedFood) {
			System.out.println("Applications Submitted:");

			if (appliedFafsa) {
				System.out.println("- FAFSA");
			}

			if (appliedHousing) {
				System.out.println("- Seattle Rental Housing Program");
			}

			if (appliedFood) {
				System.out.println("- Basic Food");
			}

			System.out.println();
		}

		System.out.println("Recommended Next Steps:");
		System.out.println("- Review available support programs");
		System.out.println("- Contact local assistance services if needed");
		System.out.println("- Save emergency contacts for quick access");
		System.out.println();
		System.out.println("Thank you for using the Community Support Resource Hub!");
		System.out.println("==================================");

		pressEnterToContinue();
	}

	//prints the links of financial aid
	public static void printLinks(String[] linksArray) {
		if (eligibility <= firstThreshold) {
			System.out.println(linksArray[0]);
		} else if (eligibility <= secondThreshold) {
			System.out.println(linksArray[1]);
		} else {
			System.out.println(linksArray[2]);
		}
	}

	//asks the user if a financial link should be added to the summary
	private static void askIfInSummary(int summaryIndex) {
		System.out.println("Would you like these links to be a part of your summary? (Y/N)");
		linksSummary[summaryIndex] = yesNoCheck();
	}

	//handles a user selecting a financial aid link
	private static void selections() {
		System.out.println("Would you like aid in:");
		System.out.println("Food Support (enter a)");
		System.out.println("Rent Budgeting (enter b)");
		System.out.println("Educational Funding (enter c)");
		String letter = scnr.nextLine().trim();

		switch (letter) {
		case "a": // Food
			System.out.println("According to your annual family income and the number of people in your house, here is food aid you could qualify for: ");
			printLinks(foodLinks);
			askIfInSummary(0);
			break;
		case "b": // Rent
			System.out.println("According to your annual family income and the number of people in your house, here is rent aid you could qualify for: ");
			printLinks(rentLinks);
			askIfInSummary(1);
			break;
		case "c": // education
			System.out.println("According to your annual family income and the number of people in your house, here is education aid you could qualify for: ");
			printLinks(educationLinks);
			askIfInSummary(2);
			break;
		default:
			System.out.println("Invalid choice!");
			break;
		}
	}
	
	//Checks for if the user pressed enter or another value to continue to the main menu
	private static void pressEnterToContinue() {
		System.out.print("\nPress Enter to return to the main menu...");
		scnr.nextLine();
	}
	
	// returns the result of Y/N question; yes (true) or no (false) depending on
		// what the user inputed.
	private static boolean yesNoCheck() {
		while (true) {
			String reply = scnr.nextLine();

			if (reply.equalsIgnoreCase("y")) {
				return true;
			}

			if (reply.equalsIgnoreCase("n")) {
				return false;
			}

			System.out.println("Invalid input! Please type \"Y\" or \"N\"");
		}
	}
}