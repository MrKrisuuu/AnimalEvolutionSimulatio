package Types;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;


    public String toString() {
        switch (this) {
            case NORTH:
                return "Północ";
            case NORTHEAST:
                return "Północny-Wschód";
            case EAST:
                return "Wschód";
            case SOUTHEAST:
                return "Południowy-Wschód";
            case SOUTH:
                return "Południe";
            case SOUTHWEST:
                return "Południowy-Zachód";
            case WEST:
                return "Zachód";
            case NORTHWEST:
                return "Północny-Zachód";
            default:
                return "???";
        }
    }

    public MapDirection next(){
        switch (this) {
            case NORTH:
                return MapDirection.NORTHEAST;
            case NORTHEAST:
                return MapDirection.EAST;
            case EAST:
                return MapDirection.SOUTHEAST;
            case SOUTHEAST:
                return MapDirection.SOUTH;
            case SOUTH:
                return MapDirection.SOUTHWEST;
            case SOUTHWEST:
                return MapDirection.WEST;
            case WEST:
                return MapDirection.NORTHWEST;
            case NORTHWEST:
                return MapDirection.NORTH;
            default:
                return null;
        }
    }

    public MapDirection previous(){
        switch (this) {
            case NORTH:
                return MapDirection.NORTHWEST;
            case NORTHEAST:
                return MapDirection.NORTH;
            case EAST:
                return MapDirection.NORTHEAST;
            case SOUTHEAST:
                return MapDirection.EAST;
            case SOUTH:
                return MapDirection.SOUTHEAST;
            case SOUTHWEST:
                return MapDirection.SOUTH;
            case WEST:
                return MapDirection.SOUTHWEST;
            case NORTHWEST:
                return MapDirection.WEST;
            default:
                return null;
        }
    }

    public Vector2d toUnitVector(){
        switch (this) {
            case NORTH:
                return new Vector2d(0,1);
            case NORTHEAST:
                return new Vector2d(1,1);
            case EAST:
                return new Vector2d(1,0);
            case SOUTHEAST:
                return new Vector2d(1,-1);
            case SOUTH:
                return new Vector2d(0,-1);
            case SOUTHWEST:
                return new Vector2d(-1,-1);
            case WEST:
                return new Vector2d(-1,0);
            case NORTHWEST:
                return new Vector2d(-1,1);
            default:
                return null;
        }
    }
}