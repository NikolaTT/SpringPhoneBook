angular.module('hello', ['ngRoute'])
    .config(function($routeProvider, $httpProvider) {

        $routeProvider.when('/', {
                templateUrl: 'home.html',
                controller: 'navigation',
                controllerAs: 'controller'
            }).when('/login', {
                templateUrl: 'login.html',
                controller: 'navigation',
                controllerAs: 'controller'
            }).when('/addContact', {
                templateUrl: 'addContact.html',
                controller: 'addcontact',
                controllerAs: 'controller'
            }).when('/showContacts', {
                templateUrl: 'showContacts.html',
                controller: 'showcontacts',
                controllerAs: 'controller'
            }).when('/exportContacts', {
                templateUrl: 'exportContacts.html',
                controller: 'exportcontacts',
                controllerAs: 'controller'
            }).when('/searchContacts', {
                templateUrl: 'searchContacts.html',
                controller: 'searchcontacts',
                controllerAs: 'controller'
            }).when('/register', {
                templateUrl: 'register.html',
                controller: 'register',
                controllerAs: 'controller'
            })
            .otherwise('/');
    })
    .controller('navigation',

        function($rootScope, $http, $location) {

            var self = this

            var authenticate = function(credentials, callback) {

                var username = credentials ? credentials.username : "";
                var password = credentials ? credentials.password : "";

                $http.post('authenticate', {
                    'username': username,
                    'password': password
                }).then(function(response) {
                    if (response.data.response == "OK") {
                        $rootScope.authenticated = true;

                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback();
                }, function() {
                    $rootScope.authenticated = false;
                    callback && callback();
                });

            }

            self.credentials = {};
            self.login = function() {
                authenticate(self.credentials, function() {
                    if ($rootScope.authenticated) {
                        $location.path("/");
                        self.error = false;
                    } else {
                        $location.path("/login");
                        self.error = true;
                    }
                });
            };

            self.logout = function() {
                $http.post('logout', {}).finally(function() {
                    $rootScope.authenticated = false;
                    $location.path("/");
                });
            };
        })
    .controller('addcontact',
        function($rootScope, $http, $location) {
            var self = this;

            self.contact = {};
            self.addContact = function() {
                if (self.contact) {
                    $http.post('addContactSpring', {
                        'firstName': self.contact.firstName,
                        'lastName': self.contact.lastName,
                        'phoneNumber': self.contact.phoneNumber
                    }).then(function(response) {
                        $location.path("/showContacts");
                    });
                }
            };
        })
    .controller('showcontacts',
        function($rootScope, $http, $location) {
            var self = this;

            $http.get('showContactsSpring').then(function(response) {
                if (response.data.data.length > 0) {
                    self.contacts = response.data;
                    $rootScope.hasContacts = true;
                }
            });
        })
    .controller('exportcontacts',
        function($rootScope, $http, $location, $window) {
            var self = this;

            self.exportAllContacts = function() {
                $window.open('/exportAllContacts');
            }

            self.exportSingleContact = function() {
                $window.open('/exportContact/' + self.contactNumber.number);
            }

        })
    .controller('searchcontacts',
        function($rootScope, $http, $location) {
            var self = this;

            $http.get('getSearches').then(function(response) {
                if (response.data.data.length > 0) {
                    self.searches = response.data;
                }
            });

            self.searchByFirstName = function() {

                self.search.param = self.search.firstName;

                $rootScope.searchByFirstName = true;
                $rootScope.searchByLastName = false;
                $rootScope.searchByPhoneNumber = false;

                $http.get('showContactsSpring').then(function(response) {
                    if (response.data.data.length > 0) {
                        self.contacts = response.data;
                    }
                });

                $http.post('addSearch', {
                    'type': "first name",
                    'query': self.search.param,
                }).then(function(response) {
                    $http.get('getSearches').then(function(response) {
                        if (response.data.data.length > 0) {
                            self.searches = response.data;
                        }
                    });
                });
            }

            self.searchByLastName = function() {

                self.search.param = self.search.lastName;

                $rootScope.searchByFirstName = false;
                $rootScope.searchByLastName = true;
                $rootScope.searchByPhoneNumber = false;


                $http.get('showContactsSpring').then(function(response) {
                    if (response.data.data.length > 0) {
                        self.contacts = response.data;
                    }
                });

                $http.post('addSearch', {
                    'type': "last name",
                    'query': self.search.param,
                }).then(function(response) {
                    $http.get('getSearches').then(function(response) {
                        if (response.data.data.length > 0) {
                            self.searches = response.data;
                        }
                    });
                });
            }

            self.searchByPhoneNumber = function() {

                self.search.param = self.search.phoneNumber;

                $rootScope.searchByFirstName = false;
                $rootScope.searchByLastName = false;
                $rootScope.searchByPhoneNumber = true;


                $http.get('showContactsSpring').then(function(response) {
                    if (response.data.data.length > 0) {
                        self.contacts = response.data;
                    }
                });

                $http.post('addSearch', {
                    'type': "phone number",
                    'query': self.search.param,
                }).then(function(response) {
                    $http.get('getSearches').then(function(response) {
                        if (response.data.data.length > 0) {
                            self.searches = response.data;
                        }
                    });
                });
            }
        })
    .controller('register',

        function($rootScope, $http, $location) {
            var self = this;

            self.registerData = {};
            self.regerror = false;
            self.regsuccess = false;

            self.register = function() {
                if (self.registerData) {
                    $http.post('registerAccount', {
                        'username': self.registerData.regUsername,
                        'password': self.registerData.regPassword
                    }).then(function(response) {
                        if (response.data.response == "NO") {
                            self.regerror = true;

                        } else {
                            self.regsuccess = true;
                            self.regerror = false;
                        }
                    });
                }
            }
        });