import React, { useState, useEffect } from "react";
import { updateEvent } from "../services/eventService";
import { GoogleLogin, GoogleOAuthProvider } from "@react-oauth/google";

const AddToGoogleCalendar = ({ event }) => {
  const [isGapiLoaded, setIsGapiLoaded] = useState(false); // State to track if gapi is loaded

  useEffect(() => {
    // Load Google Identity Services
    const loadGsiClient = () => {
      window.google.accounts.id.initialize({
        client_id: "10851869906-253fen308qpejr49o075hr2gokoso5kt.apps.googleusercontent.com", // 替换为您的客户端 ID
        callback: handleCredentialResponse,
      });

      window.google.accounts.id.renderButton(
        document.getElementById("signInButton"),
        { theme: "outline", size: "large" } // 可以自定义按钮样式
      );
    };

    // Dynamically load the Google Identity Services script
    const script = document.createElement("script");
    script.src = "https://accounts.google.com/gsi/client";
    script.async = true;
    script.onload = loadGsiClient;
    document.body.appendChild(script);

    // Load Google API client after gapi script has loaded
    const loadGapiClient = () => {
      window.gapi.load("client:auth2", () => {
        window.gapi.client
          .init({
            apiKey: "AIzaSyCNbybV_ZBl2Q_zOpqu1msp63bJ6foSQwg", // 替换为您的API密钥
            clientId: "10851869906-253fen308qpejr49o075hr2gokoso5kt.apps.googleusercontent.com", // 替换为您的客户端ID
            scope: "https://www.googleapis.com/auth/calendar",
          })
          .then(() => {
            setIsGapiLoaded(true); // gapi client loaded successfully
          })
          .catch((error) => {
            console.error("Error loading gapi client:", error);
          });
      });
    };

    // Dynamically load the gapi.js script
    const gapiScript = document.createElement("script");
    gapiScript.src = "https://apis.google.com/js/api.js";
    gapiScript.async = true;
    gapiScript.onload = loadGapiClient;
    document.body.appendChild(gapiScript);

    return () => {
      const scripts = document.querySelectorAll(
        'script[src="https://accounts.google.com/gsi/client"], script[src="https://apis.google.com/js/api.js"]'
      );
      scripts.forEach((script) => script.remove());
    };
  }, []);

  // Handle the credential response after login
  const handleCredentialResponse = (response) => {
    const credential = response.credential;
    console.log("Credential received: ", credential);
    // You can send the credential to your backend for further processing
    if (isGapiLoaded) {
      createEvent();
    }
  };

  // Create an event in the Google Calendar
  const createEvent = () => {
    console.log("66666666666666666");
  
    if (!window.gapi || !window.gapi.client || !window.gapi.client.calendar) {
      console.error("Google API client is not initialized or not loaded properly.");
      return;
    }
  
    const addevent = {
      summary: "Meeting with Bob",
      location: "Conference Room",
      description: "Discuss the new project",
      start: {
        dateTime: "2025-02-05T10:00:00-07:00",
        timeZone: "America/Los_Angeles",
      },
      end: {
        dateTime: "2025-02-05T11:00:00-07:00",
        timeZone: "America/Los_Angeles",
      },
      reminders: {
        useDefault: false,
        overrides: [
          { method: "popup", minutes: 10 },
          { method: "popup", minutes: 20 },
        ],
      },
    };
  
    // Call the Google Calendar API to create an event
    window.gapi.client.calendar.events
      .insert({
        calendarId: "primary", // Primary calendar
        resource: addevent,
      })
      .then((response) => {
        console.log("Event created:", response);
      })
      .catch((error) => {
        console.error("Error creating event:", error);
      });
  };
  

  const [userInfo, setUserInfo] = useState(null);

//   // 处理用户登录后返回的数据
//   const handleLoginSuccess = (response) => {
//     console.log("Login Success:", response);

//     // 获取用户数据，可以通过访问 Google API 获取用户信息
//     // 在这里的 response 是 Google 登录成功返回的令牌
//     // 在实际应用中，通常需要使用这个令牌来获取用户信息
//     const accessToken = response.credential; // 访问令牌

//     fetch("https://www.googleapis.com/oauth2/v3/userinfo", {
//       method: "GET",
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     })
//       .then((res) => res.json())
//       .then((data) => {
//         setUserInfo(data);
//       })
//       .catch((err) => console.log("Error fetching user info:", err));
//   };

  // 处理登录失败
  const handleLoginFailure = (error) => {
    console.error("Login Failed:", error);
  };
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
        {/* 显示用户信息 */}
        {userInfo && (
          <div>
            <h3>Welcome, {userInfo.name}</h3>
            <p>Email: {userInfo.email}</p>
            <img src={userInfo.picture} alt="profile" />
          </div>
        )}
      </div>
      <div>
        <h1>Google Calendar Event</h1>
        <div id="signInButton"></div>
      </div>
    </div>
  );
};

export default AddToGoogleCalendar;
