# 𝗘𝘅𝗽𝗲𝗻𝘀𝗲 𝗧𝗿𝗮𝗰𝗸𝗲𝗿 𝗖𝗟𝗜

<br/>

<div align="center">
  <img src="assets/cli.gif" alt="Expense Tracker CLI Demo" width="700" />
</div>
<br/>

## Overview

ExpenseTrackerCLI is a straightforward Java-based command-line application designed to cleanly record, manage, and calculate your daily expenses. It features a responsive terminal interface and ensures your records are persistently stored locally.

## Features

- Add expenses specifying description, amount, category, and date.
- View all recorded expenses safely in a structured format.
- Filter expenses and display them by isolated categories.
- Calculate total expenses across your entire record.
- Persistent data storage backed by a local flat file.

## Project Structure

```
├── src/
│   ├── Main.java           # Entry point and menu navigation
│   ├── Expense.java        # Expense data model
│   ├── ExpenseManager.java # Logic for expense operations
│   └── ConsoleUI.java      # Terminal interaction and text formatting
├── data/
│   └── expenses.csv        # Local data storage file
└── README.md               # Application documentation
```

## Usage

Compile the Java source files:
```bash
javac src/*.java -d .
```

Run the compiled application:
```bash
java Main
```

## Data Format

Data is reliably stored using a plain text structure where fields are separated by delimiters, making it simple to parse and review manually if necessary.
