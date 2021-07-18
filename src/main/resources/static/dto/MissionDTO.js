class MissionDTO extends BaseDTO {
    constructor() {
        super();
        this.id = null;
        this.name = null;
        this.description = null;
        this.time = null;
        this.numOfStar = null;
        this.numOfCoinGiftBox = null;
        this.numOfTimeGiftBox = null;
        this.type = null;
        this.usernames = [];
    }
}