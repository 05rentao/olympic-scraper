# Summer Olympics Data Scraper

This Java program extracts and analyzes data from Wikipedia's **Summer Olympics** pages using live web scraping. It does **not** use any saved files—everything is pulled from the web in real time using HTTP requests and parsed using regular expressions.

## Overview

The program prompts users to select a question related to the Summer Olympics and retrieves the answer by scraping and parsing the relevant Wikipedia pages. It is fully interactive and handles variable inputs like years and country names.

## Approach

The core of this program relies on **regular expressions (regex)** to extract relevant information directly from raw HTML. This includes matching specific elements such as table rows, links, headers, and nested spans or divs. Regex was used instead of a full HTML parser to keep the implementation simple and focused, though it may be sensitive to changes in page structure.

## Questions Answered

As of the current version of the Wikipedia pages (accessed in April 2025), this program can answer:

1. What Olympic sports start with the letter **C**?
2. Which countries have participated in the Olympics but are now considered **obsolete**?
3. Which countries won at least **6 silver medals in 2016**?
4. Which countries had **podium sweeps in 2012**?
5. How many **total medals** has the **United States won in Volleyball**?
6. How many **Olympic sport governing bodies** are headquartered in **Switzerland**?
7. Among all **Summer Olympics hosted in the United States since 1960**, how many **countries did the longest torch relay** pass through?
8. Which host country totaled the most number of scandals, controversies, and incidents while they were hosting the Summer Olympics?  

> Note: These answers are based on the live structure of Wikipedia pages at the time of development. If the structure or formatting of the pages changes in the future, some functionality may break or require updating.

## Running the Program

To run the program:

```bash
javac Main.java
java Main
