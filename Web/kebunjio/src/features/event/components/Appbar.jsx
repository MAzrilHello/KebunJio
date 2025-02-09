import React from 'react';
import { Link } from 'react-router-dom';

const Appbar = () => {
    return (
        <nav className="bg-green-800 text-white">
            <div className="container mx-auto px-4">
                <div className="flex justify-between items-center h-16">
                    <Link to="/" className="text-xl font-bold">KebunJio</Link>
                    <div className="flex space-x-6 items-center">
                        <Link to="/forum" className="hover:text-green-200">Forum</Link>
                        <Link to="/allotments" className="hover:text-green-200">Allotments</Link>
                        <Link to="/events" className="hover:text-green-200">Events</Link>
                        <Link to="/user-profile">
                            <div className="w-8 h-8 bg-gray-200 rounded-full"></div>
                        </Link>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Appbar;