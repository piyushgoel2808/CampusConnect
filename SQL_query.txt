-- 1. Create the Database and Select it
CREATE DATABASE IF NOT EXISTS chatbot;
USE chatbot;

-- -----------------------------------------------------
-- 2. Table structure for table `administrators`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `administrators` (
  `admin_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)
);

-- -----------------------------------------------------
-- 3. Table structure for table `alumni`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `alumni` (
  `alumni_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `batch` INT NOT NULL, -- Year of passing (e.g., 2018)
  `department` VARCHAR(100) NOT NULL,
  `company` VARCHAR(100) DEFAULT NULL,
  `designation` VARCHAR(100) DEFAULT NULL,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`alumni_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)
);

-- -----------------------------------------------------
-- 4. Table structure for table `announcements`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `announcements` (
  `announcement_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `date_posted` DATE NOT NULL,
  `posted_by` VARCHAR(50) NOT NULL, -- Likely links to administrator username
  PRIMARY KEY (`announcement_id`)
);

-- -----------------------------------------------------
-- 5. Table structure for table `chatbot_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chatbot_data` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `keyword` VARCHAR(100) NOT NULL,
  `reply` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- 6. Table structure for table `events`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `events` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `event_name` VARCHAR(255) NOT NULL,
  `event_date` DATE NOT NULL,
  `event_venue` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- 7. Table structure for table `faqs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `faqs` (
  `faq_id` INT NOT NULL AUTO_INCREMENT,
  `question` TEXT NOT NULL,
  `answer` TEXT NOT NULL,
  `keywords` VARCHAR(255) DEFAULT NULL, -- To help the chatbot search
  PRIMARY KEY (`faq_id`)
);

-- -----------------------------------------------------
-- 8. Table structure for table `job_opportunities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `job_opportunities` (
  `job_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL,
  `company` VARCHAR(100) NOT NULL,
  `location` VARCHAR(100) NOT NULL,
  `type` VARCHAR(50) NOT NULL, -- e.g., Internship, Job
  `department` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  `posted_date` DATE NOT NULL,
  `posted_by` VARCHAR(50) NOT NULL, -- Likely links to alumni username
  PRIMARY KEY (`job_id`)
);

-- -----------------------------------------------------
-- 9. Table structure for table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)
);



USE chatbot;

-- -----------------------------------------------------
-- 1. Insert data into `administrators`
-- -----------------------------------------------------
INSERT INTO `administrators` (`admin_id`, `username`, `password`, `email`) VALUES
(1, 'admin_raj', 'Raj@123', 'raj.mehta@college.edu'),
(2, 'admin_neha', 'Neha@123', 'neha.kapoor@college.edu');

-- -----------------------------------------------------
-- 2. Insert data into `alumni`
-- -----------------------------------------------------
INSERT INTO `alumni` (`alumni_id`, `name`, `email`, `batch`, `department`, `company`, `designation`, `username`, `password`) VALUES
(1, 'Amit Sharma', 'amit.sharma@alumni.edu', 2018, 'Computer Science', 'Infosys', 'Software Engineer', 'amit_s', 'Amit@123'),
(2, 'Priya Verma', 'priya.verma@alumni.edu', 2019, 'Electronics', 'TCS', 'System Analyst', 'priya_v', 'Priya@123'),
(3, 'Rohan Gupta', 'rohan.gupta@alumni.edu', 2020, 'Mechanical', 'Mahindra', 'Design Engineer', 'rohan_g', 'Rohan@123'),
(4, 'Sneha Iyer', 'sneha.iyer@alumni.edu', 2017, 'IT', 'Wipro', 'Senior Developer', 'sneha_i', 'Sneha@123'),
(5, 'Vikram Nair', 'vikram.nair@alumni.edu', 2018, 'Civil', 'L&T', 'Project Engineer', 'vikram_n', 'Vikram@123'),
(6, 'Ananya Das', 'ananya.das@alumni.edu', 2021, 'Biotechnology', 'Biocon', 'Research Associate', 'ananya_d', 'Ananya@123'),
(7, 'Harshad Patil', 'harshad.patil@alumni.edu', 2016, 'Electrical', 'Reliance', 'Maintenance Lead', 'harshad_p', 'Harshad@123'),
(8, 'Kavita Reddy', 'kavita.reddy@alumni.edu', 2019, 'Computer Science', 'Accenture', 'Data Analyst', 'kavita_r', 'Kavita@123'),
(9, 'Mohit Singh', 'mohit.singh@alumni.edu', 2020, 'IT', 'Tech Mahindra', 'Cloud Engineer', 'mohit_s', 'Mohit@123'),
(10, 'Divya Joshi', 'divya.joshi@alumni.edu', 2021, 'Electronics', 'Intel', 'Hardware Engineer', 'divya_j', 'Divya@123');

-- -----------------------------------------------------
-- 3. Insert data into `chatbot_data`
-- -----------------------------------------------------
INSERT INTO `chatbot_data` (`id`, `keyword`, `reply`) VALUES
(1, 'hello', 'Hi there! How can I help you today?'),
(2, 'thanks', 'You\'re most welcome!'),
(3, 'help', 'You can ask about alumni events or contact info.');

-- -----------------------------------------------------
-- 4. Insert data into `faqs`
-- -----------------------------------------------------
INSERT INTO `faqs` (`faq_id`, `question`, `answer`, `keywords`) VALUES
(1, 'How do I register on the alumni portal?', 'You can register by clicking on the \"Sign Up\" button on the homepage.', 'register,signup,join'),
(2, 'I forgot my password. How can I reset it?', 'Go to the login page and click on \"Forgot Password\" to reset it.', 'password,forgot,reset,login'),
(3, 'How can I contact the administrator?', 'You can contact the admin team via email at admin@college.edu.', 'contact,admin,help,email,support'),
(4, 'Where can I see upcoming events?', 'You can ask me \"Show upcoming events\" or click on the Events tab.', 'events,upcoming,show,fest,seminar'),
(5, 'How do I post a job opportunity as an alumnus?', 'After logging in as an alumni user, go to the \"Jobs\" section and click \"Post Job\".', 'post,job,alumni,opportunity'),
(6, 'Can current students attend the alumni meet?', 'Yes, current final-year students are welcome to attend specific sessions.', 'students,alumni,meet,attend'),
(7, 'How can I update my profile information?', 'Log in to your alumni account, go to your profile and click \"Edit\".', 'profile,update,change,edit');

-- -----------------------------------------------------
-- 5. Insert data into `announcements`
-- -----------------------------------------------------
INSERT INTO `announcements` (`announcement_id`, `title`, `description`, `date_posted`, `posted_by`) VALUES
(1, 'Campus Placement Drive by Infosys', 'Infosys is visiting our campus on 25th November 2025 for recruitment.', '2025-11-10', 'admin_raj'),
(2, 'Annual Alumni Meet 2025', 'The Annual Alumni Meet is scheduled for 5th December 2025. Register now!', '2025-11-08', 'admin_neha'),
(3, 'Library Timings Extended During Exams', 'The Central Library will remain open till 11:00 PM during the final exams.', '2025-11-09', 'admin_raj'),
(4, 'Workshop on AI and Data Science', 'A 2-day workshop on Artificial Intelligence and Data Science will be held next week.', '2025-11-11', 'admin_neha'),
(5, 'Blood Donation Camp', 'A Blood Donation Camp will be organized in collaboration with Red Cross on Friday.', '2025-11-07', 'admin_raj');

-- -----------------------------------------------------
-- 6. Insert data into `users`
-- -----------------------------------------------------
INSERT INTO `users` (`user_id`, `name`, `email`, `username`, `password`) VALUES
(1, 'Rahul Khanna', 'rahul.khanna@user.edu', 'rahul_k', 'Rahul@123'),
(2, 'Meena Rao', 'meena.rao@user.edu', 'meena_r', 'Meena@123'),
(3, 'Karan Malhotra', 'karan.malhotra@user.edu', 'karan_m', 'Karan@123'),
(4, 'Ayesha Khan', 'ayesha.khan@user.edu', 'ayesha_k', 'Ayesha@123'),
(5, 'Siddharth Jain', 'siddharth.jain@user.edu', 'sid_j', 'Sid@123'),
(6, 'Pooja Deshmukh', 'pooja.deshmukh@user.edu', 'pooja_d', 'Pooja@123'),
(7, 'Ravi Menon', 'ravi.menon@user.edu', 'ravi_m', 'Ravi@123'),
(8, 'Neeraj Bansal', 'neeraj.bansal@user.edu', 'neeraj_b', 'Neeraj@123'),
(9, 'Tanya Kapoor', 'tanya.kapoor@user.edu', 'tanya_k', 'Tanya@123'),
(10, 'Aditya Sinha', 'aditya.sinha@user.edu', 'aditya_s', 'Aditya@123');

-- -----------------------------------------------------
-- 7. Insert data into `events`
-- -----------------------------------------------------
INSERT INTO `events` (`id`, `event_name`, `event_date`, `event_venue`, `description`) VALUES
(1, 'Annual Alumni Meet', '2025-12-15', 'Auditorium Hall', 'A gathering of all alumni for networking and celebration.'),
(2, 'Career Mentorship Session', '2025-11-25', 'Seminar Room 3', 'Interactive mentoring session with 2018 batch mentors.'),
(3, 'Tech Alumni Talk', '2026-01-10', 'Online', 'A virtual seminar with alumni working in top tech companies.'),
(4, 'AI & Machine Learning Symposium', '2026-02-05', 'Auditorium Hall', 'A deep dive into AI, ML, and industry use cases for students.'),
(5, 'Women in Tech Leadership Summit', '2026-03-12', 'Seminar Room 3', 'Celebrating women innovators and leaders in technology.'),
(6, 'Alumni Sports Meet', '2026-04-20', 'College Ground', 'A fun-filled sports and networking event for all alumni.'),
(7, 'Cybersecurity Awareness Workshop', '2025-12-30', 'Online', 'Best practices and tools to stay safe in the digital world.'),
(8, 'Annual Coding Hackathon', '2026-01-18', 'Computer Lab 2', 'A 24-hour competitive coding event open to students and alumni.'),
(9, 'Entrepreneurship & Startup Panel', '2026-05-10', 'Seminar Room 1', 'Successful alumni entrepreneurs share their startup journeys.'),
(10, 'Mechanical Innovations Expo', '2026-02-22', 'Mechanical Block Hall', 'Showcase of cutting-edge mechanical engineering projects.'),
(11, 'Civil Engineering Site Safety Seminar', '2026-03-05', 'Civil Dept Conference Room', 'A seminar on modern site safety protocols and standards.'),
(12, 'Electrical Vehicle Technology Workshop', '2026-06-16', 'Auditorium Hall', 'Experts discuss EV innovations, charging systems, and future tech.'),
(13, 'Data Science Career Path Session', '2026-01-25', 'Seminar Room 3', 'Industry professionals guide students on DS job preparation.'),
(14, 'Cloud Computing Fundamentals Workshop', '2026-02-14', 'Online', 'Introduction to cloud services, deployments, and architecture.'),
(15, 'Robotics & Automation Fest', '2026-04-28', 'Auditorium Hall', 'An expo featuring robotics projects and industrial automation.'),
(16, 'Tech Entrepreneur Pitch Day', '2026-05-22', 'Startup Incubation Center', 'Alumni startups pitch their ideas to mentors and investors.'),
(17, 'Full Stack Development Masterclass', '2026-03-18', 'Computer Lab 1', 'Hands-on session on frontend, backend, APIs, and databases.'),
(18, 'AI Ethics & Future of Technology Talk', '2026-06-30', 'Seminar Room 2', 'Discussion on ethical AI, safety concerns, and future trends.');

-- -----------------------------------------------------
-- 8. Insert data into `job_opportunities`
-- -----------------------------------------------------
INSERT INTO `job_opportunities` (`job_id`, `title`, `company`, `location`, `type`, `department`, `description`, `posted_date`, `posted_by`) VALUES
(1, 'Software Engineer Intern', 'Infosys', 'Pune', 'Internship', 'Computer Science', '6-month internship focusing on backend Java development.', '2025-11-10', 'amit_s'),
(2, 'Junior Data Analyst', 'Accenture', 'Bangalore', 'Job', 'IT', 'Full-time role involving SQL, Excel, and Power BI.', '2025-11-09', 'kavita_r'),
(3, 'Embedded Systems Intern', 'Intel', 'Hyderabad', 'Internship', 'Electronics', 'Hands-on internship in embedded firmware and circuit design.', '2025-11-08', 'divya_j'),
(4, 'Civil Site Engineer', 'Larsen & Toubro', 'Mumbai', 'Job', 'Civil', 'Full-time field engineer role for recent graduates.', '2025-11-07', 'vikram_n'),
(5, 'Mechanical Design Intern', 'Mahindra & Mahindra', 'Chennai', 'Internship', 'Mechanical', 'Internship in CAD design and prototype testing.', '2025-11-06', 'rohan_g'),
(6, 'Backend Developer', 'TCS', 'Chennai', 'Job', 'Computer Science', 'Work on backend microservices using Java and Spring Boot.', '2025-11-12', 'arjun_t'),
(7, 'Network Security Intern', 'Cisco', 'Pune', 'Internship', 'IT', 'Assist in configuring firewalls and analyzing security logs.', '2025-11-12', 'meera_c'),
(8, 'AI Research Assistant', 'Google', 'Hyderabad', 'Internship', 'Artificial Intelligence', 'Support AI model training and dataset management.', '2025-11-12', 'rahul_s'),
(9, 'Business Analyst', 'Deloitte', 'Bangalore', 'Job', 'Business', 'Analyze business processes and create workflow reports.', '2025-11-12', 'neha_d'),
(10, 'Frontend Developer', 'Wipro', 'Noida', 'Job', 'Computer Science', 'Develop responsive UI using React and TypeScript.', '2025-11-12', 'sumit_k'),
(11, 'Embedded Hardware Intern', 'Qualcomm', 'Chennai', 'Internship', 'Electronics', 'Assist in PCB testing and microcontroller programming.', '2025-11-12', 'divya_s'),
(12, 'Civil Site Intern', 'Shapoorji Pallonji', 'Mumbai', 'Internship', 'Civil', 'Support site inspection and construction planning.', '2025-11-12', 'rohit_m'),
(13, 'Mechanical CAD Engineer', 'Bosch', 'Coimbatore', 'Job', 'Mechanical', 'Design mechanical parts using SolidWorks and CATIA.', '2025-11-12', 'vikas_r'),
(14, 'Cloud Engineer', 'AWS', 'Bangalore', 'Job', 'IT', 'Manage cloud infrastructure and automate deployments.', '2025-11-12', 'jatin_g'),
(15, 'Digital Marketing Intern', 'Byju\'s', 'Bangalore', 'Internship', 'Marketing', 'Assist in SEO, SMM and running ad campaigns.', '2025-11-12', 'anita_j');