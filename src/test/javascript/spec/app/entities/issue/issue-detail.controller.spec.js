'use strict';

describe('Controller Tests', function() {

    describe('Issue Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockIssue, MockProject, MockTracker, MockStatus, MockPriority, MockAuthor, MockFixedVersion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockIssue = jasmine.createSpy('MockIssue');
            MockProject = jasmine.createSpy('MockProject');
            MockTracker = jasmine.createSpy('MockTracker');
            MockStatus = jasmine.createSpy('MockStatus');
            MockPriority = jasmine.createSpy('MockPriority');
            MockAuthor = jasmine.createSpy('MockAuthor');
            MockFixedVersion = jasmine.createSpy('MockFixedVersion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Issue': MockIssue,
                'Project': MockProject,
                'Tracker': MockTracker,
                'Status': MockStatus,
                'Priority': MockPriority,
                'Author': MockAuthor,
                'FixedVersion': MockFixedVersion
            };
            createController = function() {
                $injector.get('$controller')("IssueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'redmineappApp:issueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
