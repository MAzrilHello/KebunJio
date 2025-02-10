import React from 'react';
import { Link } from 'react-router-dom';

const Appbar = () => {
    return React.createElement(
        'nav',
        { className: "bg-green-800 text-white" },
        React.createElement(
            'div',
            { className: "container mx-auto px-4" },
            React.createElement(
                'div',
                { className: "flex justify-between items-center h-16" },
                [
                    React.createElement(
                        Link,
                        { to: "/", className: "text-xl font-bold", key: "logo" },
                        "KebunJio"
                    ),
                    React.createElement(
                        'div',
                        { className: "flex space-x-6 items-center", key: "links" },
                        [
                            React.createElement(
                                Link,
                                { to: "/forum", className: "hover:text-green-200", key: "forum" },
                                "Forum"
                            ),
                            React.createElement(
                                Link,
                                { to: "/allotments", className: "hover:text-green-200", key: "allotments" },
                                "Allotments"
                            ),
                            React.createElement(
                                Link,
                                { to: "/events", className: "hover:text-green-200", key: "events" },
                                "Events"
                            ),
                            React.createElement(
                                Link,
                                { to: "/user-profile", key: "profile" },
                                React.createElement(
                                    'div',
                                    { className: "w-8 h-8 bg-gray-200 rounded-full" }
                                )
                            )
                        ]
                    )
                ]
            )
        )
    );
};

export default Appbar;