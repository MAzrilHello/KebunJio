import React from 'react';
import Appbar from '../../components/Appbar';
import placeholderImage from '../../media/event-placeholder.jpeg';

const EventPage = () => {
                          const events = [
                      {
                          id: 1,
                          title: "Orchid Viewing",
                          date: "APR 14",
                          description: "Come and see beautiful orchids during their blooming season",
                          image: placeholderImage
                      },
                      {
                          id: 2,
                          title: "Planting Fruit Tutorial Session",
                          date: "AUG 20",
                          description: "Tutorial + QnA session on how to plant some fruits",
                          image: placeholderImage
                      },
                      {
                          id: 3,
                          title: "Grape Plantation and Wine Testing",
                          date: "SEP 18",
                          description: "An event about how to plant grapes in your home and making local wine.",
                          image: placeholderImage
                      }
                      ];

                          return (
                            <div>
                                <div className="min-h-screen bg-white p-8">
                                    <Appbar/>
                                                    {/* Header */}
                                                    <div className="flex justify-between items-center mb-8">
                                                    <h1 className="text-2xl font-bold text-indigo-900">Upcoming Events</h1>

                                                    <div className="flex gap-4">
                                                    {/* Search */}
                                                    <div className="relative">
                                                    <input
                                                    type="text"
                                                    placeholder="Event name"
                                                    className="border rounded p-2 pr-8"
                                                    />
                                                    <span className="absolute right-2 top-2.5 text-gray-400">×</span>
                                                    </div>

                                                    {/* Date picker */}
                                                    <div className="flex items-center border rounded p-2">
                                                    <input
                                                    type="text"
                                                    value="08/17/2025"
                                                    className="w-24"
                                                    readOnly
                                                    />
                                                    <span className="ml-2">MM/DD/YYYY</span>
                                                    </div>

                                                    {/* Results per page */}
                                                    <select className="border rounded p-2">
                                                    <option>50</option>
                                                    </select>
                                                    </div>
                                                    </div>

                                                    {/* Events Grid */}
                                                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                                                    {events.map(event => (
                                                    <div key={event.id} className="bg-white rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-shadow">
                                                    {/* Event Image */}
                                                    <div className="h-48 bg-green-200">
                                                    <img
                                                    src={event.image}
                                                        alt={event.title}
                                                        className="w-full h-full object-cover"
                                                        />
                                                        </div>

                                                    {/* Event Content */}
                                                    <div className="p-4">
                                                    <div className="text-sm text-gray-600 mb-2">{event.date}</div>
                                                    <h3 className="font-semibold text-gray-800 mb-2">{event.title}</h3>
                                                    <p className="text-sm text-gray-600 mb-4">{event.description}</p>
                                                    <a href="#" className="text-indigo-900 text-sm hover:underline">View more</a>
                                                    </div>
                                                    </div>
                                                    ))}
                                                    </div>

                                                    {/* Pagination */}
                                                    <div className="flex justify-center items-center gap-2">
                                                    <button className="px-4 py-2 border rounded text-gray-600">Previous</button>
                                                    <button className="px-4 py-2 border rounded bg-indigo-900 text-white">1</button>
                                                    <button className="px-4 py-2 border rounded text-gray-600">2</button>
                                                    <button className="px-4 py-2 border rounded text-gray-600">3</button>
                                                    <span className="px-2">...</span>
                                                    <button className="px-4 py-2 border rounded text-gray-600">67</button>
                                                    <button className="px-4 py-2 border rounded text-gray-600">68</button>
                                                    <button className="px-4 py-2 border rounded text-gray-600">Next</button>
                                                    </div>
                                                    </div>
                            </div>
                      );
                      };

export default EventPage;