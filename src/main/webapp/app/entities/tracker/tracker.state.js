(function() {
    'use strict';

    angular
        .module('redmineappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tracker', {
            parent: 'entity',
            url: '/tracker?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'redmineappApp.tracker.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tracker/trackers.html',
                    controller: 'TrackerController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tracker');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tracker-detail', {
            parent: 'entity',
            url: '/tracker/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'redmineappApp.tracker.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tracker/tracker-detail.html',
                    controller: 'TrackerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tracker');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tracker', function($stateParams, Tracker) {
                    return Tracker.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tracker',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tracker-detail.edit', {
            parent: 'tracker-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tracker/tracker-dialog.html',
                    controller: 'TrackerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tracker', function(Tracker) {
                            return Tracker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tracker.new', {
            parent: 'tracker',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tracker/tracker-dialog.html',
                    controller: 'TrackerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tracker', null, { reload: 'tracker' });
                }, function() {
                    $state.go('tracker');
                });
            }]
        })
        .state('tracker.edit', {
            parent: 'tracker',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tracker/tracker-dialog.html',
                    controller: 'TrackerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tracker', function(Tracker) {
                            return Tracker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tracker', null, { reload: 'tracker' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tracker.delete', {
            parent: 'tracker',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tracker/tracker-delete-dialog.html',
                    controller: 'TrackerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tracker', function(Tracker) {
                            return Tracker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tracker', null, { reload: 'tracker' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
