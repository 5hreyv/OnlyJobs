document.addEventListener("DOMContentLoaded", () => {
    const firebaseConfig = {
        apiKey: "AIzaSyCLAWZIy5v72My-4KMsIVRQDwOXQ-SgDl0",
        authDomain: "only-jobs-app.firebaseapp.com",
        databaseURL: "https://only-jobs-app-default-rtdb.firebaseio.com",
        projectId: "only-jobs-app",
        storageBucket: "only-jobs-app.firebasestorage.app",
        messagingSenderId: "524589331065",
        appId: "1:524589331065:web:fa4aff026ff2a80ebcdaa8",
        measurementId: "G-H0PFF0ZX8V"
        };

    // Initialize Firebase
    if (!firebase.apps.length) {
        firebase.initializeApp(firebaseConfig);
    }
    const auth = firebase.auth();
    const db = firebase.database();

    // --- UI Elements ---
    const welcomeText = document.getElementById("welcomeText");
    const jobTableBody = document.querySelector("#jobTable tbody");
    const addJobBtn = document.getElementById("addJobBtn");
    const totalJobs = document.getElementById("totalJobs");
    const openJobs = document.getElementById("openJobs");
    const closedJobs = document.getElementById("closedJobs");
    const logoutBtn = document.getElementById("logoutBtn");

    // --- Authentication ---
    auth.onAuthStateChanged(user => {
        if (user) {
            // User is signed in.
            if (welcomeText) welcomeText.textContent = `Welcome, ${user.email}`;
            listenForJobs(user.uid);
        } else {
            // User is signed out. Redirect to login.
            if (!window.location.pathname.includes("login.html")) {
                window.location.href = "login.html";
            }
        }
    });

    // --- Database Functions ---
    function listenForJobs(recruiterId) {
        const jobsRef = db.ref('jobs');
        
        // Listen for data changes in real-time
        jobsRef.on('value', (snapshot) => {
            const jobsData = snapshot.val() || {};
            const allJobs = Object.keys(jobsData).map(key => ({
                id: key, // The unique key from Firebase
                ...jobsData[key]
            }));

            // Filter to show only jobs posted by the current recruiter
            const myJobs = allJobs.filter(job => job.posted_by === recruiterId);
            
            renderJobs(myJobs);
            updateStats(myJobs);
        });
    }

    function renderJobs(jobs) {
        if (!jobTableBody) return;
        jobTableBody.innerHTML = "";
        jobs.forEach((job) => {
            const row = document.createElement("tr");
            const statusClass = job.status === "Open" ? "status-open" : "status-closed";
            const toggleButtonText = job.status === "Open" ? "Close" : "Re-open";
            row.innerHTML = `
                <td>${job.jobTitle}</td>
                <td>${job.companyName}</td>
                <td class="${statusClass}">${job.status}</td>
                <td>
                    <button onclick="toggleStatus('${job.id}', '${job.status}')">${toggleButtonText}</button>
                    <button onclick="deleteJob('${job.id}')" style="color:red;">Delete</button>
                </td>
            `;
            jobTableBody.appendChild(row);
        });
    }

    function updateStats(jobs) {
        if (!totalJobs) return;
        totalJobs.textContent = jobs.length;
        openJobs.textContent = jobs.filter(j => j.status === "Open").length;
        closedJobs.textContent = jobs.filter(j => j.status === "Closed").length;
    }

    // --- Global Functions for Buttons (must be on window object) ---
    window.toggleStatus = function(jobId, currentStatus) {
        const newStatus = currentStatus === "Open" ? "Closed" : "Open";
        db.ref('jobs/' + jobId).update({ status: newStatus });
    };

    window.deleteJob = function(jobId) {
        if (confirm("Are you sure you want to permanently delete this job posting?")) {
            db.ref('jobs/' of jobId).remove();
        }
    };
    
    // --- Event Listeners ---
    if (addJobBtn) {
        addJobBtn.addEventListener("click", () => {
            const currentUser = auth.currentUser;
            if (currentUser) {
                const newJob = {
                    jobTitle: document.getElementById("jobTitle").value.trim(),
                    companyName: document.getElementById("companyName").value.trim(),
                    location: document.getElementById("location").value.trim(),
                    description: document.getElementById("description").value.trim(),
                    posted_by: currentUser.uid, // Link job to the recruiter
                    status: "Open" // New jobs are open by default
                };
                
                if (newJob.jobTitle && newJob.companyName) {
                   db.ref('jobs').push(newJob); // push() generates a unique ID
                   // Clear input fields
                   document.getElementById("jobTitle").value = "";
                   document.getElementById("companyName").value = "";
                   document.getElementById("location").value = "";
                   document.getElementById("description").value = "";
                } else {
                    alert("Please fill in at least the Job Title and Company Name.");
                }
            }
        });
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            auth.signOut();
        });
    }
});
