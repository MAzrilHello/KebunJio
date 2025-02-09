import React, { useState, useEffect } from "react";
import { updateEvent } from "../services/eventService";
/* global gapi, google */

const AddToGoogleCalendar = ({ event }) => {
  localStorage.setItem("pendingCalendarEvent", JSON.stringify(event));
  console.log(event.startTime);
  console.log("2025-02-19T09:00:00-07:00");
  const [isAuthorized, setIsAuthorized] = useState(false);
  const [authButtonText, setAuthButtonText] = useState("Authorize");
  const [content, setContent] = useState("");

  // TODO: Replace with your client ID and API key from the Google Developer Console
  const CLIENT_ID =
    "10851869906-pugc7b8i6mkc9il7ibv32qkm5v90l6m4.apps.googleusercontent.com";
  const API_KEY = "AIzaSyCNbybV_ZBl2Q_zOpqu1msp63bJ6foSQwg";

  // API and scope configurations
  const DISCOVERY_DOC =
    "https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest";
  const SCOPES = "https://www.googleapis.com/auth/calendar";

  let tokenClient;
  let gapiInited = false;
  let gisInited = false;

  // Load Google API scripts dynamically using useEffect
  useEffect(() => {
    const loadGapiScript = () => {
      return new Promise((resolve, reject) => {
        const script = document.createElement("script");
        script.src = "https://apis.google.com/js/api.js";
        script.onload = resolve;
        script.onerror = reject;
        document.body.appendChild(script);
      });
    };

    const loadGsiScript = () => {
      return new Promise((resolve, reject) => {
        const script = document.createElement("script");
        script.src = "https://accounts.google.com/gsi/client";
        script.onload = resolve;
        script.onerror = reject;
        document.body.appendChild(script);
      });
    };

    // Load both scripts asynchronously
    Promise.all([loadGapiScript(), loadGsiScript()])
      .then(() => {
        gapiLoaded();
        gisLoaded();
      })
      .catch((error) => {
        console.error("Error loading Google scripts", error);
      });
  }, []);

  // Callback after gapi.js is loaded
  function gapiLoaded() {
    gapi.load("client", initializeGapiClient);
  }

  // Initialize Google API client
  async function initializeGapiClient() {
    await gapi.client.init({
      apiKey: API_KEY,
      discoveryDocs: [DISCOVERY_DOC],
    });
    gapiInited = true;
    maybeEnableButtons();
  }

  // Callback after Google Identity Services are loaded
  function gisLoaded() {
    tokenClient = google.accounts.oauth2.initTokenClient({
      client_id: CLIENT_ID,
      scope: SCOPES,
      callback: "", // Callback defined later
    });
    gisInited = true;
    maybeEnableButtons();
  }

  // Enable buttons if both APIs are loaded
  function maybeEnableButtons() {
    if (gapiInited && gisInited) {
      setAuthButtonText("Authorize");
    }
  }

  // Handle authorization click
  function handleAuthClick() {
    tokenClient.callback = async (resp) => {
      if (resp.error !== undefined) {
        throw resp;
      }
      setIsAuthorized(true);
      setAuthButtonText("Refresh");
      await createEvent();
    };

    if (gapi.client.getToken() === null) {
      // Prompt the user to select a Google Account and ask for consent
      tokenClient.requestAccessToken({ prompt: "consent" });
    } else {
      // Skip the account chooser and consent dialog for an existing session
      tokenClient.requestAccessToken({ prompt: "" });
    }
  }

  // Handle sign-out click
  function handleSignoutClick() {
    const token = gapi.client.getToken();
    if (token !== null) {
      google.accounts.oauth2.revoke(token.access_token);
      gapi.client.setToken("");
      setContent("");
      setAuthButtonText("Authorize");
      setIsAuthorized(false);
    }
  }
  function convertToLocalTimezoneDate(dateString) {
    // 创建一个 Date 对象
    let date = new Date(dateString);

    // 获取当前时区的偏移，以分钟为单位
    let offset = date.getTimezoneOffset();

    // 将分钟偏移转换为时区偏移（小时和分钟）
    let sign = offset > 0 ? "-" : "+"; // 偏移符号
    let hoursOffset = Math.floor(Math.abs(offset) / 60); // 小时部分
    let minutesOffset = Math.abs(offset) % 60; // 分钟部分

    // 构造时区偏移字符串
    let timezoneOffset = `${sign}${String(hoursOffset).padStart(
      2,
      "0"
    )}:${String(minutesOffset).padStart(2, "0")}`;

    // 使用 toISOString() 格式化时间，去掉 "Z" 并添加时区偏移
    let formattedDate = `${date.toISOString().slice(0, 19)}${timezoneOffset}`;

    return formattedDate;
  }

  // Create an event
  async function createEvent() {
    // const event = {
    //   summary: "Sample Event",
    //   location: "Some location",
    //   description: "This is a sample event created via Google Calendar API.",
    //   start: {
    //     dateTime: "2025-02-19T09:00:00-07:00",
    //     timeZone: "America/Los_Angeles",
    //   },
    //   end: {
    //     dateTime: "2025-02-19T10:00:00-07:00",
    //     timeZone: "America/Los_Angeles",
    //   },
    // };
    localStorage.setItem("pendingCalendarEvent", JSON.stringify(event));
    const newevent = {};
    newevent.summary = event.title;
    newevent.location = event.location;
    newevent.description = event.description;
    newevent.start = {};
    newevent.start.dateTime = convertToLocalTimezoneDate(event.startTime);
    newevent.end = {};
    newevent.end.dateTime = convertToLocalTimezoneDate(event.endTime);

    const request = gapi.client.calendar.events.insert({
      calendarId: "primary",
      resource: newevent,
    });

    request.execute((event) => {
      setContent(`Event created: ${event.htmlLink}`);
    });
  }

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [showAlreadyAddedMessage, setShowAlreadyAddedMessage] = useState(false); // 新增状态管理

  const handleAddToCalendar = async () => {
    setLoading(true);
    setError(null);

    // 判断 event.description 是否包含 "The Google account has been successfully added"
    if (
      event.description &&
      event.description.includes(
        "The Google account has been successfully added"
      )
    ) {
      // 如果包含，弹出提示框
      setShowAlreadyAddedMessage(true);
      setLoading(false); // 结束加载状态
      return; // 结束函数，不进行后续操作
    }

    try {
      // const response = await fetch('/api/google/calendar/auth-url');
      // const data = await response.json();

      // if (data.error) {
      //     throw new Error(data.error);
      // }

      localStorage.setItem("pendingCalendarEvent", JSON.stringify(event));
      // 打开 Google 日历页面
      window.open("https://calendar.google.com/calendar/u/0/r?pli=1", "_blank");

      // 延迟2秒后显示成功提示
      setTimeout(() => {
        localStorage.setItem("calendarEventSuccess", "true");
        setShowSuccessMessage(true);
      }, 2000); // 2秒延迟
    } catch (err) {
      setError("Failed to connect to Google Calendar. Please try again.");
      console.error("Error:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCloseSuccessMessage = () => {
    setShowSuccessMessage(false);
    // 更新 event.description
    const fetchEvent = async () => {
      try {
        const newEvent = { ...event };
        newEvent.description =
          "The Google account has been successfully added " +
          new Date().toISOString().slice(0, 19).replace("T", " ") +
          "." +
          event.description;
        await updateEvent(event.id, newEvent);
      } catch (error) {
        console.error("Error fetching event:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchEvent();
  };

  const handleCloseAlreadyAddedMessage = () => {
    setShowAlreadyAddedMessage(false); // 关闭“已经添加”提示框
  };

  //   CSS
  const styles = {
    container: {
      padding: "20px",
      backgroundColor: "#f9f9f9",
      borderRadius: "8px",
      boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
      maxWidth: "500px",
      margin: "auto",
      fontFamily: "'Arial', sans-serif",
    },
    title: {
      textAlign: "center",
      color: "#333",
      marginBottom: "20px",
    },
    buttonContainer: {
      display: "flex",
      justifyContent: "space-evenly",
      marginBottom: "20px",
    },
    authButton: {
      backgroundColor: "#007BFF",
      color: "#fff",
      border: "none",
      padding: "10px 20px",
      borderRadius: "5px",
      cursor: "pointer",
      fontSize: "16px",
      transition: "background-color 0.3s",
    },
    signOutButton: {
      backgroundColor: "#FF5733",
      color: "#fff",
      border: "none",
      padding: "10px 20px",
      borderRadius: "5px",
      cursor: "pointer",
      fontSize: "16px",
      transition: "background-color 0.3s",
      opacity: 0.7,
    },
    authButtonHover: {
      backgroundColor: "#0056b3",
    },
    signOutButtonHover: {
      backgroundColor: "#c13d1a",
    },
    content: {
      whiteSpace: "pre-wrap",
      wordBreak: "break-word",
      backgroundColor: "#eaeaea",
      padding: "15px",
      borderRadius: "5px",
      fontSize: "14px",
      lineHeight: "1.5",
      boxShadow: "inset 0 2px 4px rgba(0, 0, 0, 0.1)",
    },
  };

  return (
    <div>
      <button
        onClick={handleAddToCalendar}
        disabled={loading}
        className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-6 rounded-md transition duration-200 w-full disabled:bg-blue-300 disabled:cursor-not-allowed"
      >
        {loading ? "Connecting..." : "Add to my Google Calendar"}
        {error && <p className="text-sm text-red-100 mt-2">{error}</p>}
      </button>

      {/* 显示已添加消息 */}
      {showAlreadyAddedMessage && (
        <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded-md">
            <p className="text-green-500 mb-4">
              Google account has already been added. Please do not add it again.
            </p>
            <button
              onClick={handleCloseAlreadyAddedMessage}
              className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md"
            >
              Confirm
            </button>
          </div>
        </div>
      )}

      {/* 显示成功消息 */}
      {showSuccessMessage && (
        <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-white p-4 rounded-md">
            <p className="text-green-500 mb-4">
              The event has been successfully saved
            </p>
            <button
              onClick={handleCloseSuccessMessage}
              className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md"
            >
              Confirm
            </button>
          </div>
        </div>
      )}

      <div>
        <div style={styles.container}>
          <h2 style={styles.title}>Google Calendar API Quickstart</h2>

          {/* Render authorization and signout buttons */}
          <div style={styles.buttonContainer}>
            <button onClick={handleAuthClick} style={styles.authButton}>
              {authButtonText}
            </button>
            <button
              onClick={handleSignoutClick}
              disabled={!isAuthorized}
              style={styles.signOutButton}
            >
              Sign Out
            </button>
          </div>

          {/* Display event creation result */}
          <pre style={styles.content}>{content}</pre>
        </div>
      </div>
    </div>
  );
};

export default AddToGoogleCalendar;
