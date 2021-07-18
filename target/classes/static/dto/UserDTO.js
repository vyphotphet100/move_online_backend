class UserDTO extends BaseDTO {
    constructor() {
        super();
        this.username = null;
        this.password = null;
        this.fullname = null;
        this.email = null;
        this.facebookLink = null;
        this.phoneNumber = null;
        this.address = null;
        this.accountBalance = null;
        this.numOfStar = null;
        this.numOfCoinGiftBox = null;
        this.numOfTimeGiftBox = null;
        this.numOfDefaultTime = null;
        this.referredUsernames = [];
        this.referrerUsername = null;
        this.commission = null;
        this.tokenCode = null;
        this.roleCodes = [];
        this.momoPhoneNumber = null;
        this.missionIds = [];
        this.sentMessageIds = [];
        this.receivedMessageIds = [];
        this.withdrawRequestIds = [];
    }

}